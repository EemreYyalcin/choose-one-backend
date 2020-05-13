package com.chooseone.data.redis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Pairs {

    private String client1;

    private KeyList pairItems = new KeyList();

    private LocalDateTime time = LocalDateTime.now();

}
