package com.chooseone.data.redis.model.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestDocument {

    private String type;

    private String category;

    @Override
    public String toString() {
        return type.toLowerCase() + "_" + category.toLowerCase();
    }
}
