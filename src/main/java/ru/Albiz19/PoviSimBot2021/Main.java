package ru.Albiz19.PoviSimBot2021;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.Albiz19.PoviSimBot2021.Bot;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        Bot bot = new Bot();
    }
}