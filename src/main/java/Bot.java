import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private boolean gamemode = false; //флаг, отвечающий за текущую настройку игры: в процессе/в меню
    private final boolean adminmode = false; //флаг, отвечающий за админ мод
    private Hangman hangman;
    private final KeyboardRow keyboardFirstRow = new KeyboardRow(); //Первая строка клавиатуры
    private final KeyboardRow keyboardSecondRow = new KeyboardRow();//Вторая строка
    private final List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>(); //Список строк клавиатуры

    Login login = new Login();

    public String input(String msg, SendMessage sendMessage){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();

       /*
        if (msg.contains("WordEdit mode")){
            adminmode = true;
            return "Words edit mode on, you can add a new word. Format: \n" +
                    "Example \n\n" +
                    "DO NOT forget to exit admin mode by typing: 'WordEdit mode off'"; }
        */
        if (msg.contains("КАК СКАЖЕШЬ") || msg.contains("Попытки закончились")){
            keyboardFirstRow.add(new KeyboardButton("Давай сыграем!")); //Первая кнопка в первую строку клавитуры
            keyboardSecondRow.add(new KeyboardButton("Напомнишь правила?")); //Добавление второй кнопки во вторую строчку клавы

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow); //обе кнопки добавляем в список
            replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
            return "Ожидаю выбор...";
        }
        if (msg.equals("Привет")){
            sendMessage.setText("Привет! Я - бот ПовиCим. Сыграем в виселицу?");
            try {
                execute(sendMessage);
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
            System.out.println("Давай сработал");
            return "Начали. Я загадал слово: " + hangman.getLetters() + " Твоя буква?";
            //тут будет метод, реализующий начало игры
        }
        if (msg.equals("Напомнишь правила?")){
            System.out.println("Правила сработали");
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
    public String inGameInput(String msg, SendMessage sendMessage){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();
        keyboardFirstRow.add(new KeyboardButton("Я передумал играть. Верни меня в меню.")); //Первая кнопка
        // в первую строку клавитуры
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
        msg = msg.toUpperCase();
        if (msg.length() > 1 || ((msg.charAt(0) < 'А') || (msg.charAt(0) > 'Я'))){
            keyboardFirstRow.add(new KeyboardButton("Я передумал играть. Верни меня в меню.")); //Первая кнопка
            // в первую строку клавитуры
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
            System.out.print(msg);
            if (msg.equals("Я ПЕРЕДУМАЛ ИГРАТЬ. ВЕРНИ МЕНЯ В МЕНЮ.")){
                gamemode = false;
                System.out.print("Game break event");
                keyboard.clear();
                keyboardFirstRow.clear();
                keyboardSecondRow.clear();
                return "Как скажешь. Дай знать, если захочешь сыграть.";
            }

            return "Упс! Это не буква! Для действий во время игры рекомендую использовать команды. (которых пока нет)";
        }
        char message = msg.charAt(0);
        System.out.print(message); //NOTE: начать делать после завершения отгадывания возврат в меню, начать делать прерывание игры
        msg = hangman.main(message);
        if (msg.contains("Ура!")){
            gamemode = false;
        }
        return msg;
    }
    public void setGamemode(boolean flag_tmp){
        this.gamemode = flag_tmp;
    }
    public String outPut(SendMessage sendMessage){
    return null;
    }
    public String getBotUsername() {
        return "@PoviSimBOT";
    }
    public String getBotToken() {
        return "";
    }
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        String tmp = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        if (!gamemode && !adminmode) {
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
        if (tmp.contains("Как скажешь.") || tmp.contains("Попытки закончились") || tmp.contains("Ура! Ты угадал")){
            gamemode = false;
            hello_bot(sendMessage);
        }
    }
    public void hello_bot(SendMessage sendMessage){
        System.out.println("endgame event");
        sendMessage.setText("Выход в главное меню... " +
                "Для продолжения выберите команду. \nОжидаю выбор...");
        keyboardFirstRow.add(new KeyboardButton("Давай сыграем!")); //Первая кнопка в первую строку клавитуры
        keyboardSecondRow.add(new KeyboardButton("Напомнишь правила?")); //Добавление второй кнопки во вторую строчку клавы

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow); //обе кнопки добавляем в список
        replyKeyboardMarkup.setKeyboard(keyboard); //Установливаем список нашей клавиатуре
        try{
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
