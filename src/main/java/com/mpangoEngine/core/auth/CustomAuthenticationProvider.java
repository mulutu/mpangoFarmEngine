package com.mpangoEngine.core.auth;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		if (shouldAuthenticateAgainstThirdPartySystem(name, password)) {

			// use the credentials
			// and authenticate against the third-party system
			return new UsernamePasswordAuthenticationToken(name, password, new ArrayList());
		} else {
			return null;
		}
	}

	private boolean shouldAuthenticateAgainstThirdPartySystem(String name, String password) {
		return true;
	}
}