package com.thc.sprbasic2025.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WebConnect {
	public static Map<String, Object> get(String url, Map<String, Object> param) {
		String finalUrl = url;
		String queryParams = null;
		if(param != null && !param.isEmpty()) {
			queryParams = param.entrySet().stream()
					.map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
							+ "="
							+ URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8))
					.collect(Collectors.joining("&"));

			if(url.contains("?")){
				finalUrl = url + "&" + queryParams;
			} else {
				finalUrl = url + "?" + queryParams;
			}
		}
        System.out.println(finalUrl);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);
		String body = response.getBody();
		return toMap(body);
	}
	public static Map<String, Object> post(String url, Map<String, Object> map_header, Map<String, Object> param) {

        RestTemplate restTemplate = new RestTemplate();


        // HttpHeaders 생성 및 값 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(map_header != null && !map_header.isEmpty()) {
            map_header.forEach((key, value) -> headers.set(key, value.toString()));
        }

        Map<String, Object> param_post = new HashMap<>();


        // HttpEntity에 헤더 + 바디 결합
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(param_post, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 응답 출력
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        String body = response.getBody();

        return toMap(body);
	}
	/*public static Map<String, Object> post2(String url, Map<String, Object> param) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> postResponse = restTemplate.postForEntity(url, param, String.class);
		String body = postResponse.getBody();
		return toMap(body);
	}*/

	public static Map<String, Object> toMap(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = null;
		try{
			map = objectMapper.readValue(
					json, new TypeReference<Map<String, Object>>() {}
			);
		} catch (Exception e){
		}
		return map;
	}
}
