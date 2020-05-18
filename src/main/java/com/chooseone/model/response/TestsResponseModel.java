package com.chooseone.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TestsResponseModel {

    private String type;

    private String category;

}
