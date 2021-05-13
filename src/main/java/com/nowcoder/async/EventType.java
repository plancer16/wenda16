package com.nowcoder.async;

/**
 * @author plancer16
 * @create 2021/5/13 15:42
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5);


    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
