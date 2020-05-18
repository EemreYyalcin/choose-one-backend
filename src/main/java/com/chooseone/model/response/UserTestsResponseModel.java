package com.chooseone.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class UserTestsResponseModel {

    private TestsResponseModel test;

    private int remaining = 10;

}
