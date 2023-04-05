//package com.example.damnedtgbot.services;
//
//import com.example.damnedtgbot.repo.SessionRepo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class AnswerSenderService {
//    private final SessionRepo sessionRepo;
//
//    private final RabbitTemplate template;
//
//    @Value("${sr.rabbit.queue.name}")
//    private String queueName;
//
//    @Value("${sr.rabbit.routing.name}")
//    private String routingName;
//
//    @Value("${sr.rabbit.exchange.name}")
//    private String exchangeName;
//    @PostConstruct
//    public void sendToQueue(){//produce
//        log.info("sendQueue method");
//
//        System.out.println("The JSON representation of Object mobilePhone is ");
//        ObjectMapper Obj = new ObjectMapper();
//        try {
//            String jsonStr = Obj.writeValueAsString("test");
//            System.out.println(jsonStr);
//            template.convertAndSend(exchangeName,routingName,jsonStr);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("-- end of the method --");
//
//    }
//}
