package com.meet.sample.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WSController {

    @MessageMapping("/hello")
    @SendTo("/topic/greeting")
    public String greeting(String message){
        return "hello,"+message;
    }
}
