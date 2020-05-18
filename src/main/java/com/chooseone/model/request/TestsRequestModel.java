package com.chooseone.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
public class TestsRequestModel {

    @NotEmpty(message = "type is not null or empty")
    private String type;

    @NotEmpty(message = "category is not null or empty")
    private String category;

    private String questionId;

    private int response = 99;

}
