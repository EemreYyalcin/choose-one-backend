package com.chooseone.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PairResponseModel {

    private String item1;

    private String item2;

    private Integer rate;

    private String pairId;

}
