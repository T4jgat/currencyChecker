package kz.exchangeCourse.usdkzt.telegramBot;

import kz.exchangeCourse.usdkzt.services.CurrencyParserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final BotConfig botConfig;


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

        String currency = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            CurrencyParserService currencyParserService = new CurrencyParserService();

            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                default -> {
                    currency = currencyParserService.getCurrency(messageText);
                    sendMessage(chatId, currency);
                }
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
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

        sendMessage(chatId, answer);
        sendMessage(chatId, availableRates);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.enableHtml(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }

}
