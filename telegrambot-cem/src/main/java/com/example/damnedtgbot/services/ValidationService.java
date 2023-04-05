package com.example.damnedtgbot.services;

import com.example.damnedtgbot.repo.QuestionLocaleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

//@Component
@Slf4j
public class ValidationService {
    @Autowired
    KeyboardButtonsImpl keyboardButtons;
    @Autowired
    QuestionLocaleRepo questionLocaleRepo;

//    private SendMessage takeLangAnswers(String messageText, Long chatId, int stage){
//        log.warn("takeLangAnswers - method :" + messageText);
//        int langId = returnTypeOfLanguage(messageText);
//        List<QuestionLocale> question = questionLocaleRepo.findAllByLanguage_Id(langId);
//        log.warn("exiting takeLangAnswers - method" + question.size());
//        return  new SendMessage(chatId.toString(),question.get(stage).getText() + " ");
//    }
    public Integer returnTypeOfLanguage(ReplyKeyboardMarkup messageText){
        String text = messageText.toString();
        if (text.startsWith("AZE")){
            return 1;
        } else if (text.startsWith("ING") || text.startsWith("ENG")) {
            return 3;
        }
        else if (text.startsWith("RU") || text.startsWith("Ру")){
            return 2;
        }
        else { return  0 ;}
    }


}
