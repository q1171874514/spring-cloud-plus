package com.example.controller;

import com.example.comment.TestAnnotation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    private Integer a = 0;

    @TestAnnotation("11")
    @PostMapping("/post")
    public Map<String, Object> post(@RequestBody Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    //    @TestAnnotation("11")
    @PostMapping("/body")
    public Map<String, Object> body(@RequestBody Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @PostMapping("/params")
    public Map<String, Object> params(@RequestParam Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @PostMapping("/headers")
    public Map<String, Object> headers(ServletRequest request, @RequestHeader Map<String, Object> map) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @GetMapping("/headers")
    public Map<String, Object> headerss(ServletRequest request, @RequestHeader Map<String, Object> map) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        map.put("test", a++);
        return map;
    }
}
