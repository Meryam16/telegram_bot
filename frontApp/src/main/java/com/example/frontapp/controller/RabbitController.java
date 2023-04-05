package com.example.frontapp.controller;

import com.example.frontapp.entity.Answer;
import com.example.frontapp.entity.User;
import com.example.frontapp.repos.UserRepo;
import com.example.frontapp.services.AnswerSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class RabbitController {
    private final AnswerSenderService answerSenderService;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;


    @PostMapping("/post")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {

//        JSONObject jsonObject = new JSONObject(message);

        answerSenderService.sendToQueue(message);
        return ResponseEntity.ok("it's a okay message");
    }

    @GetMapping("/get")
    public String getMock(){
         return "working!!!";
    }
    @GetMapping("/getall")
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }


}
