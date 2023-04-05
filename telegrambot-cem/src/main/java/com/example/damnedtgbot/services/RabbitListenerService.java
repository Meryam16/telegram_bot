package com.example.damnedtgbot.services;

//import com.example.frontapp.entity.OperatorAnswer;
import com.example.damnedtgbot.entity.Answer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitListenerService {
    private final Bot bot;


    @RabbitListener(queues = "front-queue")
    public void onMessage(String answer){

        JSONObject jsonObject = new JSONObject(answer);


        String text = jsonObject.getString("text");
        String chatId = jsonObject.getString("chatId");

        log.warn("**" + answer);
        bot.rabbitMessage(text, chatId);
    }
}