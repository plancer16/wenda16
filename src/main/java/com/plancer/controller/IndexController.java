package com.plancer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author plancer16
 * @create 2021/5/7 21:54
 */
@Controller
public class IndexController {

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        return "hello";
    }
}
