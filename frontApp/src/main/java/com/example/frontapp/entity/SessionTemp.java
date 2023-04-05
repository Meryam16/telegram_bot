package com.example.frontapp.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionTemp {
    Long chatId;
    Lang lang;
    Long currentQuestion;
    List< String> answers;
}
