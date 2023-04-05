package com.example.damnedtgbot.services;

import com.example.damnedtgbot.entity.QuestionLocale;
import com.example.damnedtgbot.repo.QuestionLocaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuesitonLocaleService {
    @Autowired
    QuestionLocaleRepo questionLocaleRepo;

    public List<QuestionLocale> getAll(){
        return questionLocaleRepo.findAll();
    }
    public List<QuestionLocale> getByIdLanguageId(long id){
        return questionLocaleRepo.findAllByLanguage_Id(id);
    }
}
