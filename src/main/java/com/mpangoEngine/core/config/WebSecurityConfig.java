package com.mpangoEngine.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mpangoEngine.core.auth.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//@Autowired
	//UserDetailsService userDetailsService;

	@Bean 
	public BCryptPasswordEncoder bCryptPasswordEncoder() { 
		return new BCryptPasswordEncoder(); 
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Autowired
    private CustomAuthenticationProvider authProvider;
	
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.eraseCredentials(false);
        auth.authenticationProvider(authProvider);
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// The pages does not require login
		http.authorizeRequests().antMatchers("/*", "/login*", "/logout*", "/api**",  "/api*").permitAll();
		// /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
		// If no login, it will redirect to /login page.
		http.authorizeRequests().antMatchers("/dashboard*").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

		// For ADMIN only.
		http.authorizeRequests().antMatchers("/admin*").access("hasRole('ROLE_ADMIN')");

		// When the user has logged in as XX.
		// But access a page that requires role YY,
		// AccessDeniedException will be thrown.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

		// Config for Login Form
		http.authorizeRequests().and().formLogin()//
				// Submit URL of login page.
				.loginProcessingUrl("/login") // Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/dashboard")//
				.failureUrl("/login?error=true")//
				//.usernameParameter("email")//
				//.passwordParameter("password")
				// Config for Logout Page
				//.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful")
				; 
	}
}
