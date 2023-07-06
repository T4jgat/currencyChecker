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

    public String getCurrency(String messageText) {
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < 39; i++) {
            String currentCurrency = currencyPair.get(i).text().substring(0, 3);

            if (currentCurrency.equals(messageText.toUpperCase())) {
                float calculatedCurrency = Float.parseFloat(values.get(i).text());
                String formattedCurrency;

                switch (currentCurrency) {
                    case "AMD", "HUF" -> {
                        calculatedCurrency /= 10;
                        formattedCurrency = String.format("%.2f", calculatedCurrency);
                    }
                    case "IRR" -> {
                        calculatedCurrency /= 1000;
                        formattedCurrency = String.format("%.4f", calculatedCurrency);
                    }
                    case "UZS", "KRW" -> {
                        calculatedCurrency /= 100;
                        formattedCurrency = String.format("%.3f", calculatedCurrency);
                    }
                    default -> formattedCurrency = values.get(i).text();
                }

                resultBuilder.append("<b>").append(currencyPair.get(i).text()).append("</b>:  ").append(formattedCurrency);
                return resultBuilder.toString();
            }
        }

        return "Incorrect currency"; // or any other appropriate value if no match is found
    }
}
