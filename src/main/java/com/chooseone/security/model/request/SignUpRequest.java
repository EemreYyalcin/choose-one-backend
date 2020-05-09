package com.chooseone.security.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequest {
	@NotEmpty(message = "username is not empty")
	private String username;
	@NotEmpty(message = "password is not empty")
	private String password;
}