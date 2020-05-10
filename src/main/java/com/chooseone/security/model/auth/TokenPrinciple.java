package com.chooseone.security.model.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TokenPrinciple {

    private String username;

}
