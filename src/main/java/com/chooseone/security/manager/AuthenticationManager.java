package com.chooseone.security.manager;

import com.chooseone.security.enums.Role;
import com.chooseone.security.jwt.JWTUtil;
import com.chooseone.security.model.auth.TokenPrinciple;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

	private final JWTUtil jwtUtil;
	
	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		
		try {
			if (!jwtUtil.validateToken(authToken)) {
				return Mono.empty();
			}
			Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
			List<String> rolesMap = claims.get("role", List.class);
			List<GrantedAuthority> authorities = new ArrayList<>();
			TokenPrinciple tokenPrinciple = new TokenPrinciple().setUsername(claims.getSubject());
			if (rolesMap.contains(Role.ROLE_ADMIN.name())){
				for (Role role: Role.values()) {
					authorities.add(new SimpleGrantedAuthority(role.name()));
				}
				return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), tokenPrinciple, authorities));
			}
			for (String rolemap : rolesMap) {
				authorities.add(new SimpleGrantedAuthority(rolemap));
			}
			return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), tokenPrinciple, authorities));
		} catch (Exception e) {
			return Mono.empty();
		}
	}
}