package com.example.usedmarket.web.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequiredArgsConstructor
public class GreetingController {

//
//    @SendTo("/topic/chat/{roomId}")
//    @MessageMapping("/chat/{roomId}")
//    public ChatMessage chatting(@DestinationVariable("roomId") String roomId, ChatMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
////        template.convertAndSend("/topic/greetings", message);
//        return message;
//    }
//
//    @SendToUser("/queue/chat/1")
//    @MessageMapping("/chat/1")
//    public ChatMessage greeting2(ChatMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
////        template.convertAndSend("/topic/greetings", message);
//        return message;
//    }
}