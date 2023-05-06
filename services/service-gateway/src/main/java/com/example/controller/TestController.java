package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
//多例模式
//@Scope("prototype")
public class TestController {
    private Integer a = 0;
    @PostMapping("/post")
    public Map<String, Object> post(@RequestBody Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }
}