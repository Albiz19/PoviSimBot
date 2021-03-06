package ru.Albiz19.PoviSimBot2021.res;

import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
public enum Stickers { //Stickers enum
    OOGWAY_BYE("CAACAgIAAxkBAAECFudgWIcv_fVTVHrsNaq26E_cjkzMcgACmwwAAhr6wEmcAAE5X4z7HxoeBA"),
    OOGWAY_MYTIMEHASCOME("CAACAgIAAxkBAAECFxJgWN4mFgWCDnZJMH0If69ZyHBgpwACpw0AArJ7yEoBxs3p8Lxo3h4E"),
    OOGWAY_HELLO("CAACAgIAAxkBAAECG0FgXZGXeQFjSnrplO28HmjajdRbegACmQsAAp-X6EpoG3O20uIdCB4E"),
    OOGWAY_ERROREQUAL1("CAACAgIAAxkBAAECMQVgeUYr62Ilm5RnTzH6PlYGDhNzZwACHQ0AAv5XyUuxVnwKkTykPh8E"),
    OOGWAY_ERROREQUAL2("CAACAgIAAxkBAAECTFxgnPrw8K2HnrFkDV-eQm-tiM3_kQACAxIAAoH7wEjjEQfL_QoTrR8E"),
    OOGWAY_ERROREQUAL3("CAACAgIAAxkBAAECJVNgasNePjy_Gv_I-LxoBsn_yalqnQACEQsAAlaTEUuDG16jb63nPR4E"),
    OOGWAY_ERROREQUAL4("CAACAgIAAxkBAAECTGBgnPsn-IuzL9zDddk3zR2WJDvu4wACJQ0AAhjU6EjpZD30OlAG2R8E"),
    OOGWAY_ERROREQUAL5("CAACAgIAAxkBAAECTF5gnPsNnGQEh1EpE3g86EOb4-4aOgACnxAAAlC-wEhmBqw7xqbrOB8E"),
    ;

    String stickerId;

    Stickers(String stickerId) {
        this.stickerId = stickerId;
    }
    public SendSticker getSendSticker(Long chatId) { //Send Sticer with chatid
        if ("".equals(chatId)) throw new IllegalArgumentException("ChatId cant be null");
        SendSticker sendSticker = getSendSticker();
        sendSticker.setChatId(chatId);
        return sendSticker;
    }

    public SendSticker getSendSticker() { //Send sticker
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(stickerId);
        return sendSticker;
    }
}


