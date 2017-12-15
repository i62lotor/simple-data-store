package org.geowe.datastore.configuration.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geowe.datastore.user.AppUser;
import org.geowe.datastore.user.AppUserRepository;
import org.geowe.datastore.user.security.SdsUserDetails;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final ObjectMapper objectMapper;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
			AppUserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			final AppUser user = objectMapper.readValue(req.getInputStream(), AppUser.class);
			final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					user.getLogin(), user.getPassword());
			return authenticationManager.authenticate(authentication);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		final String username = auth.getName();
		final String token = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + " " + token);
		res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		final AppUser user = ((SdsUserDetails) auth.getPrincipal()).getUser();
		user.setPassword(null);
		user.setLogin(username);
		objectMapper.writeValue(res.getWriter(), user);
	}
}