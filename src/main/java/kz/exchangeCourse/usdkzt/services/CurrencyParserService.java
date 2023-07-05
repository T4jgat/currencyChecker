package kz.exchangeCourse.usdkzt.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class CurrencyParserService {
    private final Document doc = Jsoup.connect("https://nationalbank.kz/ru/exchangerates/ezhednevnye-oficialnye-rynochnye-kursy-valyut").get();
    private final Elements els = doc.select("tbody");
    private final Elements values = els.select("td:eq(3)");
    private final Elements currencyPair = els.select("td:eq(2)");

    public CurrencyParserService() throws IOException {
    }

//    public String getCurrency(String messageText) {
//        try {
//            CurrLabel currLabel = CurrLabel.valueOf(messageText.toUpperCase());
//
//
//            return switch (currLabel) {
//                case USD -> currencyPair.get(10).text() + ":  " + values.get(10).text();
//                case EUR -> currencyPair.get(11).text() + ":  " + values.get(11).text();
//                case RUB -> currencyPair.get(24).text() + ":  " + values.get(24).text();
//                case TRY -> currencyPair.get(29).text() + ":  " + values.get(29).text();
//                case UAH -> currencyPair.get(31).text() + ":  " + values.get(31).text();
//                case NIGGER -> "NIGGER / KZT:  0,032";
//            };
//
//        } catch (IllegalArgumentException e) {
//            return "Incorrect currency";
//        }
//    }

    public String getCurrency(String messageText) {
        for (int i = 0; i < 39; i++) {
            String currentCurrency = currencyPair.get(i).text().substring(0, 3);
            if (currentCurrency.equals(messageText.toUpperCase())) {
                if (currentCurrency.startsWith("AMD") ||
                        currentCurrency.startsWith("HUF")) {
                    float calculatedCurrency = Float.parseFloat(values.get(i).text()) / 10;
                    return "<b>" + currencyPair.get(i).text() + "</b>:  " + String.format("%.2f", calculatedCurrency);
                }
                else if (currentCurrency.startsWith("IRR")) {
                    float calculatedCurrency = Float.parseFloat(values.get(i).text()) / 1000;
                    return "<b>" + currencyPair.get(i).text() + "</b>:  " + String.format("%.4f", calculatedCurrency);
                }
                else if (currentCurrency.startsWith("UZS") ||
                        currentCurrency.startsWith("KRW")) {
                    float calculatedCurrency = Float.parseFloat(values.get(i).text()) / 100;
                    return "<b>" + currencyPair.get(i).text() + "</b>:  " + String.format("%.3f", calculatedCurrency);
                }

                return "<b>" + currencyPair.get(i).text() + "</b>:  " + values.get(i).text();
            }
        }
        return "Incorrect currency";
    }

    public static void main(String[] args) throws IOException {
        CurrencyParserService parserService = new CurrencyParserService();
//        System.out.println(parserService.getCurrencyTest("irr"));
    }
}
