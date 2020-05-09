package com.chooseone.security.service;

import com.chooseone.data.redis.repository.UserRepository;
import com.chooseone.security.config.PBKDF2Encoder;
import com.chooseone.security.enums.Role;
import com.chooseone.data.redis.model.UserInfo;
import com.chooseone.security.model.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PBKDF2Encoder passwordEncoder;

	@PostConstruct
	public void init(){
		userRepository.save(new UserInfo("user", "3DJZJNXRSE9BRB/9lUo8AME8Y/si0oosAs+6w5zMUgQ=", true, Arrays.asList(Role.ROLE_ADMIN), false)).subscribe();
	}
	
	public Mono<UserInfo> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Mono<UserInfo> saveUser(SignUpRequest signUpRequest){
		UserInfo userInfo = new UserInfo();
		userInfo.setEnabled(true);
		userInfo.setRoles(new ArrayList<Role>(){{add(Role.ROLE_USER);}});
		userInfo.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		userInfo.setUsername(signUpRequest.getUsername());
		UserInfo errorUserInfo = new UserInfo();
		errorUserInfo.setError(true);
		return userRepository.findByUsername(signUpRequest.getUsername())
				.thenReturn(errorUserInfo)
				.switchIfEmpty(userRepository.save(userInfo));
	}


	
}