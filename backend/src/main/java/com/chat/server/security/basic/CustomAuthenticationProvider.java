package com.chat.server.security.basic;

import com.chat.server.dao.UserDao;
import com.chat.server.model.User;
import com.chat.server.security.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDao userDao;

	@Autowired
    private SecurityUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		User user = userDao.findUserByLogin(username);

		if (user == null) {
			throw new BadCredentialsException("Username not found.");
		}

		if ( !password.equals( user.getPassword() ) ) {
			throw new BadCredentialsException("Wrong password.");
		}

		UserDetails customUserDetails =  userDetailsService.loadUserByUsername( username );
		return new UsernamePasswordAuthenticationToken( customUserDetails, password, customUserDetails.getAuthorities() );
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
