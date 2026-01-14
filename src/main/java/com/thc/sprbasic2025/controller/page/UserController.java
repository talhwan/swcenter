package com.thc.sprbasic2025.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/user")
@Controller
public class UserController {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.base-url}")
    private String baseUrl;

    private final RestTemplate rest = new RestTemplate();

    /** 1) 로그인 시작: GitHub authorize로 리디렉트 */
    /*@GetMapping("/github")
    public void login(HttpServletResponse response, HttpSession session) throws IOException {
        String state = UUID.randomUUID().toString();
        session.setAttribute("oauth_state", state);

        String authorizeUrl = UriComponentsBuilder
                .fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("scope", "read:user user:email")
                .queryParam("redirect_uri", baseUrl + "/auth/github/callback")
                .queryParam("state", state)
                .build(true)
                .toUriString();

        response.sendRedirect(authorizeUrl);
    }*/

    /** 2) 콜백: code → access_token 교환 */
    @GetMapping("/githublogin1")
    public ResponseEntity<?> callback(@RequestParam String code,
                                      @RequestParam String state
    ) {

        /*String expected = (String) session.getAttribute("oauth_state");
        session.removeAttribute("oauth_state");
        if (expected == null || !expected.equals(state)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "state_mismatch"));
        }*/

        System.out.println("code : " + code);
        System.out.println("state : " + state);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", code);
        form.add("redirect_uri", baseUrl + "/user/githublogin");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(form, headers);

        ResponseEntity<Map> tokenResp = rest.postForEntity(
                "https://github.com/login/oauth/access_token", req, Map.class);

        if (!tokenResp.getStatusCode().is2xxSuccessful() || tokenResp.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "token_exchange_failed"));
        }

        String accessToken = (String) tokenResp.getBody().get("access_token");
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "no_access_token", "details", tokenResp.getBody()));
        }

        // 세션에 저장(데모용)
        //session.setAttribute("gh_token", accessToken);

        // 프론트 페이지로 보내거나 바로 사용자 정보 보여주기
        return ResponseEntity.ok(Map.of("login", "ok"));
    }

    /** 3) 로그인된 사용자 정보 읽기 */
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        String token = (String) session.getAttribute("gh_token");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "not_logged_in"));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        // 3-1) 기본 유저 정보
        ResponseEntity<Map> userResp = rest.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("user", userResp.getBody());

        // 3-2) 이메일 보강(비공개일 수 있음)
        ResponseEntity<List> emailsResp = rest.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );
        out.put("emails", emailsResp.getBody());

        System.out.println("out : " + out);

        return ResponseEntity.ok(out);
    }

    /** 4) 로그아웃(세션 토큰 제거) */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("logout", "ok"));
    }


    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return "user/" + page;
    }
    @RequestMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "user/" + page;
    }
}
