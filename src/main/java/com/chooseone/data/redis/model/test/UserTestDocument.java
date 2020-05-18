package com.chooseone.data.redis.model.test;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class UserTestDocument {

    private List<UserTestInfo> tests = new ArrayList<>();

}
