package kz.exchangeCourse.usdkzt.telegramBot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface IBot {
    void sendMessageWithKeyboard(Long chatId, String textToSend, ReplyKeyboardMarkup thisKeyboardMarkup);
    void sendMessageWithoutKeyboard(Long chatId, String textToSend);

    String[][] nextPageShift(String[][][] currencyList);
    String[][] prevPageShift(String[][][] currencyList);
}
