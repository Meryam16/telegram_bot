package com.example.damnedtgbot.services;
import com.example.damnedtgbot.services.abstracts.MessageService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Service
public class SendMessageImpl implements MessageService {

    @Override
    public SendMessage sendFirstMessage(long chatId) {
//        SendMessage message = new SendMessage();
//        message.setText("chose the input Language");
        return null;
    }

    @Override
    public @NonNull String sendLanguageSelector(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Salam Bota xosh gelib sefalar getirmisiz (Kidding!!!) " + "\n" +
                             "                \n"   +
                            "Zəhmət olmasa dil seçin." + "\n" +
                            "Please, select a language." + "\n" +
                            "Пожалуйста, выберите язык.");
        return sendMessage.getText();
    }
}
