package com.example.damnedtgbot.services;

import com.example.damnedtgbot.config.BotConfig;
import com.example.damnedtgbot.entity.*;
import com.example.damnedtgbot.redis.RedisDataRepo;
import com.example.damnedtgbot.redis.RedisEntity;
import com.example.damnedtgbot.redis.RedisService;
import com.example.damnedtgbot.repo.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final SessionRepo sessionRepo;
    private final RedisDataRepo redisDataRepo;
    private final UserRepo userRepo;

    public Bot(BotConfig botConfig,
               UserRepo userRepo,
               RedisDataRepo redisDataRepo,
               SessionRepo sessionRepo) {
        this.botConfig = botConfig;

        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "Start bot"));
        commandList.add(new BotCommand("/stop", "You pressed the stop button! \n" +
                "the bot has stopped! "));
        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Buttons in Bot class's constructor" + e.getMessage());
        }
        this.userRepo = userRepo;
        this.redisDataRepo = redisDataRepo;
        this.sessionRepo = sessionRepo;
    }

    private BotConfig botConfig;
    @Autowired
    private QuestionLocaleRepo questionLocaleRepo;
    @Autowired
    private QuesitonRepo questionRepo;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Autowired
    private RedisService redisService;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasContact() || update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            Long chatId = update.getMessage().getChatId();
            sendMessage.setChatId(String.valueOf(chatId));
            Optional<RedisEntity> redisEntity = redisService.findByChatId(chatId);
            String textMessage = null;

            if (update.getMessage().hasContact()) {
                System.out.println(update.getMessage().getContact().getPhoneNumber());
                registerUser(update.getMessage());
                sendLang(chatId);
            } else
                textMessage = update.getMessage().getText();

            if (textMessage != null)
                switch (textMessage) {
                    case "/start":
                        if (redisEntity.isEmpty()) {
                            getContact(sendMessage);
                        } else {
                            sendMessage.setText(getContinueMessage(redisEntity));
                            execute(sendMessage);
                        }
                        break;
                    case "/stop":
                        sendMessage.setText("You pressed the /stop button \n" +
                                "Hint - Press /start to start the bot. ");
                        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                        execute(sendMessage);
                        redisService.remove(chatId);
                        break;
                    default:
                        switch (textMessage) {
                            case "AZ":
                                redisEntity.get().setLang(Lang.AZ);
                                sendNextQuestion(redisEntity.get());
                                break;
                            case "RU":
                                redisEntity.get().setLang(Lang.RU);
                                sendNextQuestion(redisEntity.get());
                                break;
                            case "EN":
                                redisEntity.get().setLang(Lang.EN);
                                sendNextQuestion(redisEntity.get());
                                break;
                            default:
                                if (validation(redisEntity.get(), textMessage)) {
                                    QuestionLocale questionLocale = questionLocaleRepo.findByLanguageIdAndAndQuestionId(redisEntity.get().getLang().getId(), redisEntity.get().getCurrentQuestion());
                                    redisEntity.get().getAnswers().put(questionLocale.getText(), textMessage);
                                    sendNextQuestion(redisEntity.get());
                                } else {
                                    sendMessage.setText(sendValidationFailedMessage(redisEntity.get()));
                                    execute(sendMessage);
                                }
                                break;
                        }
                }
        }
    }

    private boolean validation(RedisEntity redisEntity, String textMessage) {
        QuestionLocale questionLocale = questionLocaleRepo.findByLanguageIdAndAndQuestionId(redisEntity.getLang().getId(), redisEntity.getCurrentQuestion());

        List<String> optionList = questionLocale.getOptions().stream().map(option -> option.getAnswer()).collect(Collectors.toList());
        if (questionLocale.getQuestion().getType().equals("BUTTON")) {
            if (optionList.contains(textMessage))
                return true;
            else
                return false;
        } else
            return true;
    }

    private String sendValidationFailedMessage(RedisEntity redisEntity) {
        if (redisEntity.getLang() != null) {
            return switch (redisEntity.getLang()) {
                case AZ -> "Zəhmət olmasa verilmiş seçimlərə uyğun cavablayın.";
                case EN -> "Please choose answer given on buttons.";
                case RU -> "Пожалуйста, ответьте согласно предложенным вариантам";
            };
        } else {
            return "Zəhmət olmasa verilmiş seçimlərə uyğun cavablayın.";
        }

    }

    private void sendLang(Long chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        KeyboardButton az = new KeyboardButton("AZ");
        KeyboardButton en = new KeyboardButton("EN");
        KeyboardButton ru = new KeyboardButton("RU");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(az, en, ru))));
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Xoş gördük! \uD83D\uDC4B Hansı dildə danışmaq sənə rahat olar?");
        execute(sendMessage);
    }

    private void sendNextQuestion(RedisEntity redisEntity) throws TelegramApiException {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(redisEntity.getChatId()));
        try {
            QuestionLocale questionLocale = getNextQuestion(redisEntity);
            createButton(questionLocale, sendMessage);

            redisEntity.setCurrentQuestion(questionLocale.getQuestion().getId());
            redisService.save(redisEntity);

            sendMessage.setText(questionLocale.getText());
            if (questionLocale.getQuestion().getNextQuestion() == null)
                createSession(redisEntity);
        } catch (NullPointerException e) {
            sendMessage.setText(getFinishMessage(redisEntity));
        }
        execute(sendMessage);
    }


    private void createSession(RedisEntity redisEntity) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        User user = userRepo.findById(redisEntity.getChatId()).get();
        JSONObject object = new JSONObject(redisEntity.getAnswers());
        Session session = Session.builder()
                .answers(object.toString())
                .timestamp(timestamp)
                .languageId(redisEntity.getLang().getId())
                .user(user)
                .build();

        sessionRepo.save(session);
    }

    private QuestionLocale getNextQuestion(RedisEntity redisEntity) throws NullPointerException {
        if (redisEntity.getCurrentQuestion() == null) {
            return questionLocaleRepo.findByLanguageIdAndAndQuestionId(redisEntity.getLang().getId(), 1);//findFirst question from db
        } else {
            Question question = questionRepo.findById(redisEntity.getCurrentQuestion()).get();//findById
            return questionLocaleRepo.findByLanguageIdAndAndQuestionId(redisEntity.getLang().getId(), question.getNextQuestion());
        }
    }

    private User registerUser(Message msg) {
        Optional<User> user = userRepo.findById(msg.getChatId());
        if (user.isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User newUser = User.builder()
                    .id(chatId)
                    .phoneNumber(msg.getContact().getPhoneNumber())
                    .telegramId(msg.getContact().getUserId())
                    .fullName(chat.getFirstName() + " " + chat.getLastName())
                    .build();

            redisService.save(RedisEntity.builder().chatId(newUser.getId()).isActive(true).build());
            return userRepo.save(newUser);
        } else {
            redisService.save(RedisEntity.builder().chatId(user.get().getId()).isActive(true).build());
            return userRepo.findById(msg.getChatId()).get();
        }
    }

    private void createButton(QuestionLocale questionLocale, SendMessage sendMessage) {
        List<Option> optionList = questionLocale.getOptions();

        if (questionLocale.getQuestion().getType().equals("BUTTON")) {
            var buttons = optionList.stream().map(item -> new KeyboardButton(item.getAnswer())).collect(Collectors.toList());
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(new KeyboardRow(buttons)));
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        } else
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

    }

    private String getFinishMessage(RedisEntity redisEntity) {
        return switch (redisEntity.getLang()) {
            case AZ -> "Müraciətinizə uyğun təkliflər olduqda sizə geri dönüş edəcəyik, zəhmət olmasa gözləyin.";
            case RU ->
                    "Мы свяжемся с вами, когда у нас будут предложения, подходящие для вашего приложения, пожалуйста, подождите.";
            case EN -> "We will get back to you when we have offers suitable for your application, please wait.";
        };
    }

    private String getContinueMessage(Optional<RedisEntity> redisEntity) {
        if (redisEntity.get().getLang() != null) {
            return switch (redisEntity.get().getLang()) {
                case AZ ->
                        "Sizin aktiv olan sorğunuz var. Sorğuya davam edə və ya \"/stop\" əmrini yazmaqla bu sorğunu dayandıra bilərsiniz";
                case EN ->
                        "You have an active request. You can continue the request or stop this request by typing \"/stop\".";
                case RU ->
                        "У вас есть активный запрос. Вы можете продолжить запрос или остановить его, набрав \"/stop\".";
            };
        } else
            return "Sizin aktiv olan sorğunuz var. Sorğuya davam edə və ya \"/stop\" əmrini yazmaqla bu sorğunu dayandıra bilərsiniz";
    }

    private void getContact(SendMessage sendMessage) throws TelegramApiException {
        KeyboardButton keyboardButton2 = new KeyboardButton();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(keyboardButton2))));
        keyboardButton2.setText("Nömrəni paylaş");
        keyboardButton2.setRequestContact(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Zəhmət olmasa nömrənizi paylaşın");
        execute(sendMessage);
    }



    public void rabbitMessage(String answer,String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
