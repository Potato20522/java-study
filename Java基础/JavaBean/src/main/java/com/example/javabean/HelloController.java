package com.example.javabean;

import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//91F48B86F8FFD1A492919D0247F1AAFF
@RestController
public class HelloController {
    @GetMapping("hello")
    public String hello(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return "hello";
    }
}
