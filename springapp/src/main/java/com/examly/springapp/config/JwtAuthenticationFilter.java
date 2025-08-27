package com.examly.springapp.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final MyUserDetailsService myUserDetailsService;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	public JwtAuthenticationFilter(JwtUtils jwtUtils, MyUserDetailsService myUserDetailsService) {
		this.jwtUtils = jwtUtils;
		this.myUserDetailsService = myUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtUtils.extractUsername(jwtToken);
			} catch (Exception e) {
				logger.error("Unable to extract JWT token or token is invalid");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

			if (jwtUtils.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(userDetails);

				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}
		filterChain.doFilter(request, response);

	}

}