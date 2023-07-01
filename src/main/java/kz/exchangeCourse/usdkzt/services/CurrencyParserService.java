package kz.exchangeCourse.usdkzt.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class CurrencyParserService {
    private final Document doc = Jsoup.connect("https://nationalbank.kz/ru/exchangerates/ezhednevnye-oficialnye-rynochnye-kursy-valyut").get();
    private final Elements els = doc.select("tbody");
    private final Elements els2 = els.select("td:eq(3)");

    public CurrencyParserService() throws IOException {
    }

    private enum CurrLabel {
        USD, EUR, RUB, TRY, UAH;
    }

    public String getCurrency(String messageText) {
        try {
            CurrLabel currLabel = CurrLabel.valueOf(messageText.toUpperCase());


            return switch (currLabel) {
                case USD -> els2.get(10).text();
                case EUR -> els2.get(11).text();
                case RUB -> els2.get(24).text();
                case TRY -> els2.get(29).text();
                case UAH -> els2.get(31).text();
            };

        } catch (IllegalArgumentException e){
            return "Incorrect currency";
        }
    }
}
