package com.mpangoEngine.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.service.SecurityService;
import com.mpangoEngine.core.service.UserService;
import com.mpangoEngine.core.util.CustomErrorType;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Override
	public String findLoggedInUsername() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

	public MyUser findLoggedInUser() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return (MyUser) userDetails;
		}
		return null;
	}

	@Override
	public void autologin(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, password, userDetails.getAuthorities());

		Authentication authenticatedUse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			logger.debug(String.format("Auto login %s successfully!", username));
		}
	}

	@Transactional
	public Collection<? extends GrantedAuthority> userlogin(String username, String password) {
		MyUser user = null;
		Authentication authenticatedUse = null;
		// UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		UserDetails userDetails = userService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, password, userDetails.getAuthorities());

		usernamePasswordAuthenticationToken.setDetails(userDetails);

		try {
			authenticatedUse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
			if (usernamePasswordAuthenticationToken.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				String email = usernamePasswordAuthenticationToken.getName();
				// user = userService.findUserByEmail(email);
				// user = userService.findUserByUserName(email);
				logger.info("API login userlogin... userlogin usernamePasswordAuthenticationToken: >>>>>>>> 222 {}",
						usernamePasswordAuthenticationToken);
			}
		} catch (AuthenticationException ae) {
			logger.info("API login userlogin... Authentication exception trace: {}", ae);
		}
		return usernamePasswordAuthenticationToken.getAuthorities();
	}
}
