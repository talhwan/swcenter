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

}
