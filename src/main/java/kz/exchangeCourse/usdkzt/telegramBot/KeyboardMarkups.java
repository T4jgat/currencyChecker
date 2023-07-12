package kz.exchangeCourse.usdkzt.telegramBot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMarkups {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboardRows = new ArrayList<>();
    String[][] currencyList;

    public KeyboardMarkups(String[][] currencyList) {
        this.currencyList = currencyList;
    }

    public ReplyKeyboardMarkup setKeyboardMarkup() {
        for (String[] strings : currencyList) {
            KeyboardRow row = new KeyboardRow();
            for (String string : strings) {
                row.add(string);
            }
            keyboardRows.add(row);
        }

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }


}
