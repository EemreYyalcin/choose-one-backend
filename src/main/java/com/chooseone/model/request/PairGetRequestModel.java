package com.chooseone.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PairGetRequestModel {

    @NotEmpty(message = "pairs variable is not empty or null")
    private String pairs;

}
