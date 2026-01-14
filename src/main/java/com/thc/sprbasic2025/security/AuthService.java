package com.thc.sprbasic2025.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public interface AuthService {
	
	Algorithm getTokenAlgorithm();
	String createAccessToken(Long userId);
	Long verifyAccessToken(String accessToken) throws JWTVerificationException;
	String createRefreshToken(Long userId);
	void revokeRefreshToken(Long userId);
	Long verifyRefreshToken(String refreshToken) throws JWTVerificationException;
	String issueAccessToken(String refreshToken) throws JWTVerificationException;

}