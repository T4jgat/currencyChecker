package kz.exchangeCourse.usdkzt.telegramBot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;

    @Bean
    public mPage pages() {
        return mPage.FIRST_PAGE;
    }

    public enum mPage{
        FIRST_PAGE, SECONG_PAGE, THIRD_PAGE;
    }

}
