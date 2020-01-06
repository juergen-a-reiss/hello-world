package com.solvians.helloworld;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyApi {

    @RequestMapping("/greeting")
    public String greeting(){
        return "Hello World";
    }
}
