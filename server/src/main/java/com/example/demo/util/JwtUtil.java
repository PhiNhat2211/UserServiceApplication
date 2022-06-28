package com.example.demo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtil {

	@Autowired
    private AuthenticationManager authenticationManager;

	@Autowired
    private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ResponseUtil responseUtil;
	// add new //////////////////////////////////////////////////////////////////////////////////////////////
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
	// add new //////////////////////////////////////////////////////////////////////////////////////////////
	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;
	
	@Value("${app.jwt.secret}")  // set secretKey in Application.properties
	private String secretKey;
	private String secret = "xadmin";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	// update private to public////////////////////////////////////////////////////////////////////
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		// here it will check if the token has created before time limit. i.e 10 hours
		// then will will return true else false
		return extractExpiration(token).before(new Date());
	}

	// this method is for generating token. as argument is username. so as user
	// first time send request with username and password
	// so here we will fetch the username , so based on that username we are going
	// to create one token
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
	// generate new accessToken ///////////////////////////////////////////////////////////////////////
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.setSubject(user.getId() + "," + user.getUsername())
				.claim("roles", user.getRoles().toString())
				.setIssuer("CodeJava")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	// in this method createToken subject argument is username
	// here we are setting the time for 10 hours to expire the token.
	// and you can see we are using HS256 algorithmn
	
	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	// here we are validation the token
	public Boolean validateToken(String token, UserDetails userDetails) {
		// basically token will be generated in encrpted string and from that string .
		// we extract our usename and password using extractUsername method
		final String username = extractUsername(token);
		// here we are validation the username and then check the token is expired or
		// not
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// add new ValidateACcessToken ////////////////////////////////////////////////////////////////////
	public Boolean validateAccessToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			LOGGER.error("JWT expired", ex);
		} catch (IllegalArgumentException ex) {
			LOGGER.error("Token is null, empty or has only whitespace", ex);
		} catch (MalformedJwtException ex) {
			LOGGER.error("JWT is invaled", ex);
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("JWT is not supported", ex);
		} catch (SignatureException ex) {
			LOGGER.error("Signature validation failed", ex);
		}
		return false;
	}
	
	// add new /////////////////////////////////////////////////////////////////////////////////////////////
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	// add new ////////////////////////////////////////////////////////////////////////////////////
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}

	public ResponseEntity<?> createAuthenticationToken(User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return responseUtil.failResponseEntity("Wrong username or password");
        }
        try {
            user = userService.getUserWithNameAndPass(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            return responseUtil.failResponseEntity("Unable to find user account in the database !");
        }
//        final String jwt =generateAccessToken(user.getUsername());
        final String jwt = generateToken(user.getUsername());
		String role = roleRepository.getRoleByUserId(user.getId());
        return new ResponseEntity<>(new AuthResponse(user.getId(), user.getUsername(), jwt , role, user.getEmail()), HttpStatus.OK);
//        return new ResponseEntity<>(new AuthResponse(user.getId(), user.getUsername(), jwt ), HttpStatus.OK);

    }
}
