package com.mpangoEngine.core.service;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.mpangoEngine.core.model.MyUser;

public interface SecurityService {
	String findLoggedInUsername();

	void autologin(String username, String password);
	
	Collection<? extends GrantedAuthority> userlogin(String username, String password);
}