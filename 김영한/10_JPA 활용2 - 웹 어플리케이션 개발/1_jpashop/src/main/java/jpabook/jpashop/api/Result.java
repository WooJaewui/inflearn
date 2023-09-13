package jpabook.jpashop.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Result <T> {

    private int count;
    private T result;

    public Result(int count, T result) {
        this.count = count;
        this.result = result;
    }

}
