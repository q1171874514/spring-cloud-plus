package com.example.controller;

import com.example.comment.TestAnnotation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private Integer a = 0;

    @TestAnnotation("11")
    @PostMapping("/post")
    public String post() {

        return "test" + a++;
    }
}
