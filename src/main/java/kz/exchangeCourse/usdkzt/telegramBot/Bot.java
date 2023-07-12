package kz.exchangeCourse.usdkzt.telegramBot;

import kz.exchangeCourse.usdkzt.services.CurrencyParserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot implements IBot {
    private final BotConfig botConfig;
    BotConfig.mPage pages;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        KeyboardMarkups keyboardMarkup;
        String[][][] keyboardLayout = new String[][][]{
                {
                        {"USD", "RUB", "TRY", "UAH", "PLN"},
                        {"EUR", "BYN", "GBP", "UZS", "AZN"},
                        {"AMD", "BRL", "HUF", "HKD", "next >>"}
                },
                {
                        {"<< prev", "GEL", "DKK", "AED", "INR"},
                        {"IRR", "CAD", "CNY", "KWD", "KGS"},
                        {"MYR", "MXN", "MDL", "NOK", "next >>"}
                },
                {
                        {"<< prev", "PLN", "SAR", "SGD", "TJS"},
                        {"THB", "CZK", "SEK", "CHF", "ZAR"},
                        {"KRW", "JPY"}
                }
        };

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            CurrencyParserService currencyParserService = new CurrencyParserService();

            if (messageText.equals("/start")) {
                keyboardMarkup = new KeyboardMarkups(thisPage(keyboardLayout));
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), keyboardMarkup.setKeyboardMarkup());
            }
            else if (messageText.equals("next >>")) {
                keyboardMarkup = new KeyboardMarkups(nextPageShift(keyboardLayout));
                sendMessageWithKeyboard(chatId, "next page", keyboardMarkup.setKeyboardMarkup());
            }
            else if (messageText.equals("<< prev")) {
                keyboardMarkup = new KeyboardMarkups(prevPageShift(keyboardLayout));
                sendMessageWithKeyboard(chatId, "previous page", keyboardMarkup.setKeyboardMarkup());
            } else {
                keyboardMarkup = new KeyboardMarkups(thisPage(keyboardLayout));
                sendMessageWithKeyboard(chatId, currencyParserService.getCurrency(messageText), keyboardMarkup.setKeyboardMarkup());
            }


        }
    }

    private void startCommandReceived(Long chatId, String name, ReplyKeyboardMarkup keyboardMarkup) {

        String answer =
                "Hi, " + name +
                        """
                                , nice to meet you!
                                Enter the currency whose official National Bank exchange rate
                                you want to know in relation to <b>KZT</b>.
                                For example: <b>USD</b>""";

        String availableRates = """
                <b>Available</b>:
                                
                <b>USD</b> - US Dollar
                <b>RUB</b> - Russian Ruble
                <b>TRY</b> - Turkish Lira
                <b>UAH</b> - Ukrainian Hryvna
                <b>EUR</b> - Euro
                <b>BYN</b> - Belarusian ruble
                <b>GBP</b> - Pound Sterling UK
                <b>UZS</b> - Uzbek Sums
                <b>AZN</b> - Azeri Manat
                <b>AMD</b> - Armenian Dram
                <b>BRL</b> - Brazilian Real
                <b>HUF</b> - Hungarian Forints
                <b>HKD</b> - Hong Kong Dollar
                <b>GEL</b> - Georgian Lari
                <b>DKK</b> - Danish Krone
                <b>AED</b> - Dirkham UAE
                <b>INR</b> - Indian Rupee
                <b>IRR</b> - Iranian Rial
                <b>CAD</b> - Canadian Dollar
                <b>CNY</b> - Chinese Yuan
                <b>KWD</b> - Kuwaiti Dinar
                <b>KGS</b> - Kyrgyz Som
                <b>MYR</b> - Malaysian Ringgit
                <b>MXN</b> - Mexican Peso
                <b>MDL</b> - Moldovan Leu
                <b>NOK</b> - Norwegian Krone
                <b>PLN</b> - Polish Zloty
                <b>SAR</b> - Saudi Arabian Riyal
                <b>SGD</b> - Singapore Dollar
                <b>TJS</b> - Tajik Somoni
                <b>THB</b> - Thai Baht
                <b>CZK</b> - Czech Krone
                <b>SEK</b> - Swedish Krone
                <b>CHF</b> - Swiss Franc
                <b>ZAR</b> - South African Rand
                <b>KRW</b> - South Korean Won
                <b>JPY</b> - Japanese Yen""";

        sendMessageWithoutKeyboard(chatId, answer);
        sendMessageWithKeyboard(chatId, availableRates, keyboardMarkup);
    }


    @Override
    public void sendMessageWithoutKeyboard(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.enableHtml(true);

        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {}
    }

    @Override
    public void sendMessageWithKeyboard(Long chatId, String textToSend, ReplyKeyboardMarkup thisKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(thisKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {}
    }

    @Override
    public String[][] nextPageShift(String[][][] keyboardLayout) {
        String[][] currencyList;

        pages = switch (pages) {
            case FIRST_PAGE -> BotConfig.mPage.SECONG_PAGE;
            case SECONG_PAGE -> BotConfig.mPage.THIRD_PAGE;
            case THIRD_PAGE -> BotConfig.mPage.FIRST_PAGE;
        };


        currencyList = switch (pages) {
            case FIRST_PAGE -> keyboardLayout[0];
            case SECONG_PAGE -> keyboardLayout[1];
            case THIRD_PAGE -> keyboardLayout[2];
        };

        return currencyList;
    }

    public String[][] thisPage(String[][][] keyboardLayout) {
        return switch (pages) {
            case FIRST_PAGE -> keyboardLayout[0];
            case SECONG_PAGE -> keyboardLayout[1];
            case THIRD_PAGE -> keyboardLayout[2];
        };
    }

    @Override
    public String[][] prevPageShift(String[][][] keyboardLayout) {
        String[][] currencyList;

        pages = switch (pages) {
            case FIRST_PAGE -> BotConfig.mPage.THIRD_PAGE;
            case SECONG_PAGE ->BotConfig.mPage.FIRST_PAGE;
            case THIRD_PAGE -> BotConfig.mPage.SECONG_PAGE;
        };

        currencyList = switch (pages) {
            case FIRST_PAGE -> keyboardLayout[0];
            case SECONG_PAGE -> keyboardLayout[1];
            case THIRD_PAGE -> keyboardLayout[2];
        };

        return currencyList;

    }
}
