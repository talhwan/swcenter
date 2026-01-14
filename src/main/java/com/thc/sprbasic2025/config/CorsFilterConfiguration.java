package com.thc.sprbasic2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

//헤더에 담을 내용도 확인!!
@Configuration
public class CorsFilterConfiguration {
   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.setAllowedOriginPatterns(Arrays.asList("*"));
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      String[] arrays = {"Authorization", "RefreshToken"};
      config.setAllowedHeaders(Arrays.asList(arrays));
      source.registerCorsConfiguration("/api/**", config);
      return new CorsFilter(source);
   }
}