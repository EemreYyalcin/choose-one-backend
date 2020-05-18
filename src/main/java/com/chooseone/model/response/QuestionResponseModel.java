package com.chooseone.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class QuestionResponseModel {

    private String id;

    private String item1;

    private String item2;

    private int rate = 45;

    @JsonIgnore
    private String username;

}
