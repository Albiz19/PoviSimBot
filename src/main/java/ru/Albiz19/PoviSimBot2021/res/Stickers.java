package ru.Albiz19.PoviSimBot2021.res;

import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
public enum Stickers {
    OOGWAY_BYE("CAACAgIAAxkBAAECFudgWIcv_fVTVHrsNaq26E_cjkzMcgACmwwAAhr6wEmcAAE5X4z7HxoeBA"),
    OOGWAY_MYTIMEHASCOME("CAACAgIAAxkBAAECFxJgWN4mFgWCDnZJMH0If69ZyHBgpwACpw0AArJ7yEoBxs3p8Lxo3h4E"),
    OOGWAY_HELLO("CAACAgIAAxkBAAECG0FgXZGXeQFjSnrplO28HmjajdRbegACmQsAAp-X6EpoG3O20uIdCB4E"),
    OOGWAY_ERROREQUAL3("CAACAgIAAxkBAAECJVNgasNePjy_Gv_I-LxoBsn_yalqnQACEQsAAlaTEUuDG16jb63nPR4E"),
    ;

    String stickerId;

    Stickers(String stickerId) {
        this.stickerId = stickerId;
    }
    public SendSticker getSendSticker(Long chatId) {
        if ("".equals(chatId)) throw new IllegalArgumentException("ChatId cant be null");
        SendSticker sendSticker = getSendSticker();
        sendSticker.setChatId(chatId);
        return sendSticker;
    }

    public SendSticker getSendSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(stickerId);
        return sendSticker;
    }
}


