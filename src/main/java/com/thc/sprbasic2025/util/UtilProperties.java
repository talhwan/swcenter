package com.thc.sprbasic2025.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UtilProperties {
	@Value("${mailbox.host}")
	private String host;
	@Value("${mailbox.username}")
	private String username;
	@Value("${mailbox.password}")
	private String password;
}
