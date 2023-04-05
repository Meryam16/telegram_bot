package com.example.damnedtgbot.controller;

import com.example.damnedtgbot.entity.User;
import com.example.damnedtgbot.redis.RedisEntity;
import com.example.damnedtgbot.redis.RedisService;
import com.example.damnedtgbot.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
//    @Autowired
    private final RedisService redisService;
    private final UserRepo userRepo;

    @GetMapping
    public List<RedisEntity> getAll(){
        return redisService.getAll();
    }

    @GetMapping("/{chatId}")
    public RedisEntity getByChatId(@PathVariable Long chatId){
        return redisService.findByChatId(chatId).get();
    }

    @GetMapping("/getall")
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    @DeleteMapping
    public String deleteAll(){
        redisService.clearCache();
        return "Cache cleared";
    }
}
