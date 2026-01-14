package com.thc.sprbasic2025.util;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.*;

public class GithubCommitsByToken {
	private static final URI GRAPHQL = URI.create("https://api.github.com/graphql");
	private static final ObjectMapper MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static Map<String, Object> history(String access_token, String today_date, Integer max_repo) throws Exception {
		Map<String, Object> returnValue = new HashMap<>();

		String token = access_token;
		//today_date = "2025-08-15";
		ZoneId KST = ZoneId.of("Asia/Seoul");
		LocalDate kstDate = (today_date != null) ? LocalDate.parse(today_date) : LocalDate.now(KST);
		int perRepo = (max_repo != null) ? max_repo : 100;

		Instant from = kstDate.atStartOfDay(KST).toInstant();
		Instant to   = kstDate.plusDays(1).atStartOfDay(KST).toInstant();

		String viewerId = fetchViewerId(token);                 // 1) viewer.id 조회
		List<Map<String, Object>> commits = fetchCommits(token, // 2) ID로 author 필터하여 커밋 조회
				viewerId, from, to, perRepo);

		System.out.println("date(KST): " + kstDate + "  count: " + commits.size());
		for (Map<String, Object> c : commits) {
			System.out.printf("- %s | %s | %s%n  %s%n",
					c.get("repo"),
					((String)c.get("sha")).substring(0, 7),
					c.get("committedDate"),
					c.get("url"));
		}

		returnValue.put("count", commits.size());
		returnValue.put("list", commits);

		return returnValue;
	}

	/** 1) 토큰 주인의 전역 노드 ID 조회 */
	static String fetchViewerId(String token) throws Exception {
		String q = "query { viewer { id login } }";
		JsonNode body = callGql(token, Map.of("query", q));
		JsonNode viewer = body.path("data").path("viewer");
		if (viewer.isMissingNode()) throw new RuntimeException("viewer is null");
		return viewer.get("id").asText();
	}

	static List<Map<String, Object>> fetchCommits(
			String token, String authorId, Instant from, Instant to, int perRepo
	) throws Exception {

		String query = """
      query(
        $fromDT: DateTime!, $toDT: DateTime!,
        $fromTS: GitTimestamp!, $toTS: GitTimestamp!,
        $per: Int!, $authorId: ID!
      ) {
        viewer {
          contributionsCollection(from: $fromDT, to: $toDT) {
            commitContributionsByRepository(maxRepositories: 100) {
              repository {
                nameWithOwner
                url
                defaultBranchRef {
                  name
                  target {
                    ... on Commit {
                      history(
                        first: $per,
                        since: $fromTS, until: $toTS,
                        author: { id: $authorId }
                      ) {
                        nodes {
                          oid
                          messageHeadline
                          committedDate
                          url
                          author { user { login } email }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    """;

		Map<String, Object> vars = Map.of(
				// 같은 시각 값을 DateTime/ GitTimestamp 두 타입에 각각 바인딩
				"fromDT", from.toString(),
				"toDT",   to.toString(),
				"fromTS", from.toString(),
				"toTS",   to.toString(),
				"per",    perRepo,
				"authorId", authorId
		);

		JsonNode root = callGql(token, Map.of("query", query, "variables", vars));

		List<Map<String, Object>> out = new ArrayList<>();
		var repos = root.path("data").path("viewer").path("contributionsCollection")
				.path("commitContributionsByRepository");
		if (repos.isMissingNode() || !repos.isArray()) return out;

		for (JsonNode r : repos) {
			String repo = r.path("repository").path("nameWithOwner").asText(null);
			JsonNode target = r.path("repository").path("defaultBranchRef").path("target");
			if (repo == null || target.isMissingNode() || !target.has("history")) continue;

			JsonNode nodes = target.path("history").path("nodes");
			if (!nodes.isArray()) continue;

			for (JsonNode n : nodes) {
				String sha  = n.path("oid").asText(null);
				String msg  = n.path("messageHeadline").asText(null);
				String date = n.path("committedDate").asText(null);
				String url  = n.path("url").asText(null);
				if (sha != null) {
					out.add(Map.of(
							"repo", repo,
							"sha", sha,
							"message", msg,
							"committedDate", date,
							"url", url
					));
				}
			}
		}
		return out;
	}

	/** 공통 GraphQL 호출 + 에러 처리 */
	static JsonNode callGql(String token, Map<String, Object> payload) throws Exception {
		String json = MAPPER.writeValueAsString(payload);
		HttpRequest req = HttpRequest.newBuilder(GRAPHQL)
				.header("Authorization", "Bearer " + token)
				.header("Content-Type", "application/json")
				.header("User-Agent", "CommitCollector/1.0")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();
		HttpResponse<String> resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
		if (resp.statusCode() / 100 != 2) {
			throw new RuntimeException("HTTP " + resp.statusCode() + ": " + resp.body());
		}
		JsonNode root = MAPPER.readTree(resp.body());
		if (root.has("errors")) {
			throw new RuntimeException("GraphQL errors: " + root.get("errors").toString());
		}
		return root;
	}
}
