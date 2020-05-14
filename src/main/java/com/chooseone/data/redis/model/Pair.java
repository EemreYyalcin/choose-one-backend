package com.chooseone.data.redis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Pair {

    private String item1;

    private String item2;

    private String client1;

    private Integer client1Click;

    private boolean resolved = false;

    private String parentPairs;

    @JsonIgnore
    private String id;

}
