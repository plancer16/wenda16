package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author plancer16
 * @create 2021/5/9 18:24
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User selectById(int id){
        return userDAO.selectById(id);
    }

    public User selectByName(String name){
        return userDAO.selectByName(name);
    }

    public Map<String,String> register(String name,String password){
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(name);
        if (user != null){
            map.put("msg","该用户名已注册");
            return map;
        }
        user = new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(password+user.getSalt());
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,String> login(String username,String password){
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null){
            map.put("msg","用户名不存在");
            return map;
        }
        if (!user.getPassword().equals(password + user.getSalt())){//user.getPassword() != (password + user.getSalt() string不能用==
            map.put("msg", "密码不正确，请重新输入");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //登录和注册时，添加一个该用户此时此刻登录的ticket
    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);

        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+7);
        Date date=curr.getTime();

        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
    public void logOut(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
