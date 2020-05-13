package com.chooseone.data.redis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KeyList {

    private List<String> keys = new ArrayList<>();

    public KeyList addKey(String key){
        keys.add(key);
        return this;
    }


}
