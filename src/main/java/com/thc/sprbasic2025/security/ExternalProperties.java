package com.thc.sprbasic2025.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ExternalProperties {

	@Value("${external.jwt.tokenSecretKey}")
	private String tokenSecretKey;
	
	@Value("${external.jwt.tokenPrefix}")
	private String tokenPrefix;

	@Value("${external.jwt.accessKey}")
	private String accessKey;

	@Value("${external.jwt.accessTokenExpirationTime}")
	private Integer accessTokenExpirationTime;

	@Value("${external.jwt.refreshKey}")
	private String refreshKey;
	
	@Value("${external.jwt.refreshTokenExpirationTime}")
	private Integer refreshTokenExpirationTime;

	@Value("${github.base-url}")
	private String githubBaseUrl;
	@Value("${github.client-id}")
	private String githubClientId;
	@Value("${github.client-secret}")
	private String githubClientSecret;

    @Value("${giftishow.user_id}")
	private String gUserId;
    @Value("${giftishow.custom_auth_code}")
	private String customAuthCode;
    @Value("${giftishow.custom_auth_token}")
	private String customAuthToken;
    @Value("${giftishow.dev_yn}")
	private String devYn;

}
