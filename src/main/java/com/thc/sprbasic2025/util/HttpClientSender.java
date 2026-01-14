package com.thc.sprbasic2025.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientSender {

    public static Map post(String url, Map<String, Object> param) {
        // Map -> JSON 문자열로 변환 (Jackson)
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = null;
        try{
            jsonBody = objectMapper.writeValueAsString(param);
        } catch (Exception e){
        }

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

        System.out.println("post json body: " + jsonBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        String responseBody = null;
        try{
            // 3) 요청 보내고 응답 받기 (문자열로)
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("status = " + response.statusCode());
            System.out.println("body   = " + response.body());
            responseBody = response.body();
        } catch (Exception e){

        }
        return toMap(responseBody);
    }

    public static Map get(String url, Map<String, Object> param) {
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

        // 1) 클라이언트 만들기
        HttpClient client = HttpClient.newHttpClient();

        // 2) GET 요청 만들기
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .GET()  // 기본이 GET이지만 명시해도 됨
                .build();

        String responseBody = null;

        try{
            // 3) 요청 보내고 응답 받기 (문자열로)
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("status = " + response.statusCode());
            System.out.println("body   = " + response.body());
            responseBody = response.body();
        } catch (Exception e){

        }
        return toMap(responseBody);
    }

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
