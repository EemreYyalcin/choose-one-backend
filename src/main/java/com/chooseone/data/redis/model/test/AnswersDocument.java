package com.chooseone.data.redis.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class AnswersDocument {

    private Set<String> users = new HashSet<>();

    @JsonIgnore
    public AnswersDocument addUser(String user){
        users.add(user);
        return this;
    }

}
