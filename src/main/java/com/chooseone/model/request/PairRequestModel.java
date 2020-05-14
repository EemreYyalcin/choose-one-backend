package com.chooseone.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PairRequestModel {

    private String pairId;

    private Integer clickItem;

    @NotEmpty(message = "pairsId is not null or empty")
    private String pairs;

}
