package com.example.damnedtgbot.repo;

import com.example.damnedtgbot.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option,Long> {

    @Query("SELECT o FROM Option o " +
            "INNER JOIN QuestionLocale q ON o.questionLocale.id = q.id " +
            "WHERE q.language.id = ?1")
    List<Option> findAllByLanguageId(int langId);


    @Query("SELECT o FROM Option o " +
            "INNER JOIN QuestionLocale q ON o.questionLocale.id = q.id " +
            "WHERE q.language.id = ?1 AND q.question.id = ?2")
    List<Option> findAllByLanguageIdAndStageId(int langId, int stageId);
}
