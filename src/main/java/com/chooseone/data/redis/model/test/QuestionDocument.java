package com.chooseone.data.redis.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class QuestionDocument {

    private String item1;

    private String item2;

    @JsonIgnore
    private String id;

}
