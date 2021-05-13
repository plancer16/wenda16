package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * @author plancer16
 * @create 2021/5/11 17:02
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    public void setUser(User user){users.set(user);}
    public User getUser(){return users.get();}
    public void clear(){users.remove();}
}
