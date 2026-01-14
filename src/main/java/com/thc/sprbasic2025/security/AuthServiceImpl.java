package com.thc.sprbasic2025.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.thc.sprbasic2025.domain.RefreshToken;
import com.thc.sprbasic2025.exception.InvalidTokenException;
import com.thc.sprbasic2025.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	private final ExternalProperties externalProperties;
	private final RefreshTokenRepository refreshTokenRepository;
	public AuthServiceImpl(
			ExternalProperties externalProperties
			, RefreshTokenRepository refreshTokenRepository
	) {
		this.externalProperties = externalProperties;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	public Algorithm getTokenAlgorithm() {
		return Algorithm.HMAC512(externalProperties.getTokenSecretKey());
	}

	/**
	 * 	Access Token 생성을 위한 함수.
	 *  Payload에 user Id를 담는다. 
	 *  
	 */
	@Override
	public String createAccessToken(Long userId) {
    	return JWT.create()
 			 	  .withSubject("accessToken")
 			 	  .withClaim("id", userId)
 			 	  .withExpiresAt(new Date(System.currentTimeMillis()+externalProperties.getAccessTokenExpirationTime()))
 			 	  .sign(getTokenAlgorithm());
	}

    /**
	 * 	Access Token 검증을 위한 함수
	 *   
	 */
	@Override
	public Long verifyAccessToken(String accessToken) throws JWTVerificationException {
		return JWT.require(getTokenAlgorithm())
				.build()
				.verify(accessToken)
				.getClaim("id").asLong();
	}

    /**
	 * 	Refresh Token 생성을 위한 함수.
	 *  Payload에 user Id를 담는다.
	 *  DB에 저장할수도 있음.
	 *  redis에 userId를 key로, 발급한 Refresh Token을 value로 저장할 수도있음.
	 *  
	 */
	@Override
	public String createRefreshToken(Long userId) {

		// refreshToken 기존꺼 지우기 (로그인 하면 이전 리프레시 토큰 지우는 기능)
		revokeRefreshToken(userId);

		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.YEAR, 1);
		Date date = new Date(nowCal.getTimeInMillis());

    	String refreshToken = JWT.create()
			     				 .withSubject("refreshToken")
			     				 .withClaim("id", userId)
			     				 //.withExpiresAt(new Date(System.currentTimeMillis()+externalProperties.getRefreshTokenExpirationTime()))
			     				 .withExpiresAt(date)
			     				 .sign(getTokenAlgorithm());

		//디비 저장
		refreshTokenRepository.save(RefreshToken.of(refreshToken, userId));
		//refreshTokenRepository.save(new RefreshTokenDto.CreateReqDto(refreshToken, userId).toEntity());

		return refreshToken;
	}

	/**
	 * 	Refresh Token 삭제 위한 함수.
	 *  user Id로 조회해서 모두 지운다.
	 */
	@Override
	public void revokeRefreshToken(Long userId) {
		refreshTokenRepository.deleteAll(refreshTokenRepository.findByUserId(userId));
	}

    /**
	 * 	Refresh Token 검증을 위한 함수
	 *   
	 */
	@Override
	public Long verifyRefreshToken(String refreshToken) throws JWTVerificationException {

		refreshTokenRepository.findByContent(refreshToken)
				.orElseThrow(() -> new InvalidTokenException(""));

		return JWT.require(getTokenAlgorithm())
				.build()
				.verify(refreshToken)
				.getClaim("id").asLong();
	}
	
	/**
	 * 	Access Token 발급을 위한 함수.
	 *  Refresh Token이 유효하다면 Access Token 발급.
	 *  
	 */
	@Override
	public String issueAccessToken(String refreshToken) throws JWTVerificationException {
		// Refresh Token 검증(실패시 throws JWTVerificationException)
		//System.out.println("refresh : " +refreshToken);
		Long userId = verifyRefreshToken(refreshToken);
		// Access Token 생성
		String accessToken = createAccessToken(userId);
		
		return accessToken;
	}

}