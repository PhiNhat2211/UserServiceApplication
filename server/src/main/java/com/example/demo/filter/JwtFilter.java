package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtill;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(!hasAuthorizationHeader(request)) {
			
			filterChain.doFilter(request, response);
			return;
		}
		String accessToken = getAccessToken(request);
		
		if(!jwtUtill.validateAccessToken(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}
		setAuthenticationContext(accessToken, request);
		filterChain.doFilter(request, response);

		
	}
	// add new ////////////////////////////////////////////////////////////////////////////////////////

	private void setAuthenticationContext(String accessToken, HttpServletRequest request){
		UserDetails userDetails = getUserDetails(accessToken);
		
		UsernamePasswordAuthenticationToken authentication 
			= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	private UserDetails getUserDetails(String accessToken) {
		User userDetails = new User();
		Claims claims = jwtUtill.parseClaims(accessToken);
		
		String claimRoles = (String) claims.get("roles");
		
		System.out.println("claimRoles: " + claimRoles);
		
		claimRoles = claimRoles.replace("[", "").replace("]", "");
		String[] roleNames = claimRoles.split(",");
		
		for(String aRoleName : roleNames) {
			userDetails.addRole(new Role(aRoleName));
		}
		
		String subject = (String) claims.get(Claims.SUBJECT);
//		String[] subjectArray = jwtUtill.getSubject(accessToken).split(",");
		String[] subjectArray = subject.split(",");

		
		userDetails.setId(Long.parseLong(subjectArray[0]));
		userDetails.setUsername(subjectArray[1]);
		
		return userDetails;
	}
	// add new ////////////////////////////////////////////////////////////////////////////////////////
	private Boolean hasAuthorizationHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		System.out.println("Authorization header: " + header);
		
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		return true;
	}
	// add new ////////////////////////////////////////////////////////////////////////////////////////
	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		System.out.println("Access token: " + token);
		return token;
	}
	
/*
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YWRtaW4iLCJleHAiOjE1OTUxODMyMTcsImlhdCI6MTU5NTE0NzIxN30.S9w8LROFAKYCWU2nsHu07FcWGfI5vwesC4MvsqzGDyo
		String autherizationHeader = request.getHeader("Authorization");
		String token = null;
		String username=null;
		if(autherizationHeader !=null && autherizationHeader.startsWith("Bearer"))
		{
			token = autherizationHeader.substring(7);
			username = jwtUtill.extractUsername(token);
		}
			if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				UserDetails userDetails = service.loadUserByUsername(username);
				
				if (jwtUtill.validateToken(token, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
			}
			*/
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
//	private UserDetails getUserDetails(String accessToken) {
//		User userDetails = new User();
//		Claims claims = jwtUtill.parseClaims(accessToken);
//		
//		
//	}
}
















