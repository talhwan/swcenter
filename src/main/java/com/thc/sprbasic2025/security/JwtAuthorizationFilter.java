package com.thc.sprbasic2025.security;

import com.thc.sprbasic2025.domain.User;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.function.Supplier;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserRepository userRepository;
	private final AuthService authService;
	private final ExternalProperties externalProperties;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, AuthService authService
			, ExternalProperties externalProperties
	) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.authService = authService;
		this.externalProperties = externalProperties;
	}

	/**
     *  권한 인가를 위한 함수.
     *  Access Token을 검증하고 유효하면 Authentication을 직접 생성해 SecurityContextHolder에 넣는다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String jwtHeader = request.getHeader(externalProperties.getAccessKey());
		if(jwtHeader == null || !jwtHeader.startsWith(externalProperties.getTokenPrefix())) {
			// 토큰 없을 시 Authentication 없는 상태로 doFilter -> Spring Security가 잡아낸다.
			chain.doFilter(request, response);
			return;
		}
		//String accessToken = jwtHeader.replace(externalProperties.getTokenPrefix(), "");
		String accessToken = jwtHeader.substring(externalProperties.getTokenPrefix().length());
		//System.out.println("jwtHeader!!!12 : " + accessToken);
		Long userId = authService.verifyAccessToken(accessToken);
		//System.out.println("userId : " + userId);

		// 유저 조회, 없을 시 return NoMatchingDataException(404)
		/*User userEntity = userRepository.findEntityGraphRoleTypeById(userId).orElseThrow(new Supplier<NoMatchingDataException>() {
			@Override
			public NoMatchingDataException get() {
				return new NoMatchingDataException("id : " + userId);
			}
		});*/
		User user = userRepository.findById(userId).orElse(null);
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		// Authentication 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		// SecurityContextHolder에 Authentication을 담아서 Spring Security가 권한 처리 할 수 있게 한다.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);

	}

}