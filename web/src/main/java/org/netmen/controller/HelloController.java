package org.netmen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "Hello")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "长条注释", description = "详细描述")
    public String hello(){
        return "get hello!";
    }
}
