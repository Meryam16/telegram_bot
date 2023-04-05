package com.example.damnedtgbot.repo;

import com.example.damnedtgbot.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query("SELECT l FROM Language l")
    List<Language> findAll();


    @Query("SELECT l FROM Language l WHERE lower(l.name) = ?1")
    Language findLanguageById(String langId);
}
