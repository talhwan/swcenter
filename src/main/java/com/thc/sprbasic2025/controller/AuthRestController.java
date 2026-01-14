package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.UserDto;
import com.thc.sprbasic2025.exception.InvalidTokenException;
import com.thc.sprbasic2025.security.AuthService;
import com.thc.sprbasic2025.security.ExternalProperties;
import com.thc.sprbasic2025.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthRestController {

    final ExternalProperties externalProperties;
    final AuthService authService;

    @PostMapping("")
    public ResponseEntity<Void> access(HttpServletRequest request){
        String accessToken = null;
        String prefix = externalProperties.getTokenPrefix();
        String refreshToken = request.getHeader(externalProperties.getRefreshKey());
        if(refreshToken == null || !refreshToken.startsWith(prefix)){
            throw new InvalidTokenException("no prefix");
        }
        refreshToken = refreshToken.substring(prefix.length());
        accessToken = authService.issueAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).header(externalProperties.getAccessKey(), prefix + accessToken).build();
    }

}
