package com.chooseone.security.config;

import com.chooseone.security.manager.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final AuthenticationManager authenticationManager;
	
	private final ServerSecurityContextRepository securityContextRepository;


	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
			.exceptionHandling()
			.authenticationEntryPoint((swe, e) -> {
				return Mono.fromRunnable(() -> {
					swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				});
			}).accessDeniedHandler((swe, e) -> {
				return Mono.fromRunnable(() -> {
					swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
				});
			}).and()
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.authenticationManager(authenticationManager)
			.securityContextRepository(securityContextRepository)
			.authorizeExchange()
			.pathMatchers(HttpMethod.OPTIONS).permitAll()
			.pathMatchers("/login").permitAll()
			.pathMatchers("/signUp").permitAll()
			.anyExchange().authenticated()
			.and().build();
	}
}