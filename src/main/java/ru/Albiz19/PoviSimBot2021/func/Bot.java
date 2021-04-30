package ru.Albiz19.PoviSimBot2021.func;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.Albiz19.PoviSimBot2021.res.Login;
import ru.Albiz19.PoviSimBot2021.res.Stickers;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private boolean gamemode = false; //флаг, отвечающий за текущую настройку игры: в процессе/в меню
    private Hangman hangman; //hangman отвечает за игровую логику
    private final KeyboardRow keyboardFirstRow = new KeyboardRow(); //Первая строка клавиатуры
    private final KeyboardRow keyboardSecondRow = new KeyboardRow();//Вторая строка
    private final List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>(); //Список строк клавиатуры
    private SendSticker sendSticker = new SendSticker(); //Объект для отправки стикеров
    private Long chatId; //ID чата

    //--------------! This part requires Bot token and nick input !------------------
    public String getBotUsername() {
        return "@PoviSimBOT"; //Pls, insert your telegram bot nick here
    }
    public String getBotToken() {
        return "1660604096:AAEQDbHhwYjlJ0CrfEq4fJGv5-8osmjKJLE"; //Pls, insert your telegram token here
    }


    public String input(String msg, SendMessage sendMessage){ //Обработка обычного ввода
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();
        if (msg.equals("Привет")){ //Обработка приветствия
            sendSticker = Stickers.OOGWAY_HELLO.getSendSticker(chatId);
            sendMessage.setText("Привет! Я - бот ПовиCим. Сыграем в виселицу");
            try {
                execute(sendMessage);
                execute(sendSticker);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
            keyboardFirstRow.add(new KeyboardButton("Давай сыграем!")); //Первая кнопка в первую строку клавитуры
            keyboardSecondRow.add(new KeyboardButton("Напомнишь правила?")); //Добавление второй кнопки во вторую строчку клавы

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow); //обе кнопки добавляем в список
            replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
            return "Ожидаю выбор...";
        }
        if (msg.equals("Давай сыграем!")){
            gamemode = true;
            hangman = new Hangman();
            System.out.println("StartGame event triggered");
            return "Начали. Я загадал слово: " + hangman.getLetters() + " Твоя буква?";
            //тут будет метод, реализующий начало игры
        }
        if (msg.equals("Напомнишь правила?")){
            System.out.println("Rules event triggered");
            sendMessage.setText("Все очень просто. Я загадываю слово, а ты пытаешься его отгадать. " +
                    "Каждый ход ты пишешь букву, и, если она присутствует в загаданном слове, я ее открываю. Если " +
                    "в слове нет такой буквы - будет рисоваться виселица. Ты можешь допустить 7? ошибок до того как виселица" +
                    "будет дорисована.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
            sendMessage.setText("Также, если тебе кажется, что ты знаешь слово, то ты можешь попытаться его угадать." +
                    "Для этого нажми соотвествующую кнопку в меню. Начнем?");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
            //тут будет метод, реализующий игру
            return "Начнем?";
        }
        return msg;
    }
    public String inGameInput(String msg, SendMessage sendMessage){ //Обработка сообщений после перехода в игровой режим
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        keyboardFirstRow.clear();
        msg = msg.toUpperCase();
        if (msg.length() > 1 || ((msg.charAt(0) < 'А') || (msg.charAt(0) > 'Я'))){ //Проверка неправильного ввода
            keyboardFirstRow.add(new KeyboardButton("Я передумал играть. Верни меня в меню."));
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            System.out.print("UserInput: " + msg);
            if (msg.equals("Я ПЕРЕДУМАЛ ИГРАТЬ. ВЕРНИ МЕНЯ В МЕНЮ.")){
                System.out.println("GameExit event triggered");
                gamemode = false;
                    sendSticker= Stickers.OOGWAY_BYE.getSendSticker(chatId);
                    try {
                        execute(sendSticker);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                return "Как скажешь. Дай знать, если захочешь сыграть.";
            }
            return "Упс! Это не буква! Для действий во время игры рекомендую использовать команды.";
        }
        char message = msg.charAt(0);
        System.out.println("UserInput: " + message);
        msg = hangman.main(message);
        if (msg.contains("Попытки закончились") || msg.contains("Ура! Ты угадал")){
            if (msg.contains("Попытки закончились")) {
                sendSticker = Stickers.OOGWAY_MYTIMEHASCOME.getSendSticker(chatId);
                try {
                    execute(sendSticker);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            gamemode = false;
            sendMessage.setText(msg + exitToMenu());
        }
        if (msg.contains("Не угадал!")){
            int tmp = Integer.parseInt(msg.substring(0, 1));
            System.out.println("MistakesCounter: " + tmp);
            msg = msg.substring(1);
            switch (tmp){
                case 1: {
                    sendSticker = Stickers.OOGWAY_ERROREQUAL1.getSendSticker(chatId);
                    try {
                        execute(sendSticker);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    sendSticker = Stickers.OOGWAY_ERROREQUAL3.getSendSticker(chatId);
                    try {
                        execute(sendSticker);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }
        return msg;
    }

    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage().setChatId(chatId);
        sendSticker.setChatId(chatId);
        String tmp = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        if (!gamemode) { //Не в игровом режиме
            sendMessage.setText(input(tmp, sendMessage));
        }
        else if (gamemode){
            sendMessage.setText(tmp = inGameInput(tmp,sendMessage));
        }
        try{
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public String exitToMenu(){
        System.out.println("EndGame event triggered");
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        keyboardSecondRow.clear();
        keyboardFirstRow.clear();
        keyboardFirstRow.add(new KeyboardButton("Давай сыграем!")); //Первая кнопка в первую строку клавитуры
        keyboardSecondRow.add(new KeyboardButton("Напомнишь правила?")); //Добавление второй кнопки во вторую строчку клавы
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow); //обе кнопки добавляем в список
        replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
        return "\nОжидаю выбор...";
    }
}
