package kz.exchangeCourse.usdkzt.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class CurrencyParserService {

    private enum CurrLabel {
        USD, EUR, RUB, TRY, UAH;
    }

    public static String getCurrency(String messageText) {
        Scanner sc = new Scanner(System.in);

        try {
            CurrLabel currLabel = CurrLabel.valueOf(messageText.toUpperCase());
            Document doc = Jsoup.connect("https://nationalbank.kz/ru/exchangerates/ezhednevnye-oficialnye-rynochnye-kursy-valyut").get();

            Elements els = doc.select("tbody");
            Elements els2 = els.select("td:eq(3)");

            return switch (currLabel) {
                case USD -> els2.get(10).text();
                case EUR -> els2.get(11).text();
                case RUB -> els2.get(24).text();
                case TRY -> els2.get(29).text();
                case UAH -> els2.get(31).text();
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e){
            return "Incorrect currency";
        }
    }
}
