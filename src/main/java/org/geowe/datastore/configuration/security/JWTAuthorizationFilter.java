package org.geowe.datastore.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private static final Logger logger = Logger.getLogger(JWTAuthorizationFilter.class);
	
	private final UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authManager, UserDetailsService userDetailsService) {
		super(authManager);
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		final String header = req.getHeader(SecurityConstants.HEADER_STRING);
		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		final UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(SecurityConstants.HEADER_STRING);
		if(token == null){
			return null;	
		}
		return parseTokenHeader(token);
		
	}
	
	private UsernamePasswordAuthenticationToken parseTokenHeader(String tokenHeader){
		UsernamePasswordAuthenticationToken authentication = null;
		try {
			String user = Jwts.parser().setSigningKey(SecurityConstants.SECRET)
					.parseClaimsJws(tokenHeader.replace(SecurityConstants.TOKEN_PREFIX, "")).getBody().getSubject();
			if (user != null) {
				final UserDetails userDetails = userDetailsService.loadUserByUsername(user);

				authentication = new UsernamePasswordAuthenticationToken(
						userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			}
		} catch (MalformedJwtException e) {
			logger.warn("Malformed Jwt token. Can not parse: "+tokenHeader);
		}
		return authentication;
	}

}
