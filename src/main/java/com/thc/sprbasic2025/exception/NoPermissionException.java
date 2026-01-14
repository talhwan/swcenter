package com.thc.sprbasic2025.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
권한 체크를 위한 코드
 */
@ResponseStatus(value=HttpStatus.FORBIDDEN)
@SuppressWarnings("serial")
@NoArgsConstructor
public class NoPermissionException extends RuntimeException {
	public NoPermissionException(String message) {
		super(message);
	}
}