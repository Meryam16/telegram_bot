package com.example.damnedtgbot.services;

import com.example.damnedtgbot.services.abstracts.KeyboardButtons;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardButtonsImpl implements KeyboardButtons {
    @Override
    public ReplyKeyboardMarkup languageButtons() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("English");
        keyboardRow.add("Azerbaijani");
        keyboardRow.add("Russian");

        keyboardRowList.add(keyboardRow);

        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }
}
