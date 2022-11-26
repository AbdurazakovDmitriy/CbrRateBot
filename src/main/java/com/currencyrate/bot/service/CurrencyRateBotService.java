package com.currencyrate.bot.service;

import com.currencyrate.bot.feign.CbrServiceFeign;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.currencyrate.bot.util.DateUtil.getLocalDateFromSec;

@Service
@RequiredArgsConstructor
public class CurrencyRateBotService extends TelegramLongPollingBot {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Value("${bot.name}")
    private String botUserName;

    @Value("${bot.token}")
    private String token;

    @Value("${urls.cbrServicePath}")
    private String url;

    private final CbrServiceFeign cbrServiceFeign;


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        LocalDate date = getLocalDateFromSec(update.getMessage().getDate());

        String text = update.getMessage().getText();
        if (text.equalsIgnoreCase("Дай курсы")) {
            cbrServiceFeign.getRates(date.format(DATE_FORMATTER))
                    .doOnNext(currencyRate -> {
                        try {
                            SendMessage message = new SendMessage();
                            message.setChatId(String.valueOf(update.getMessage().getChatId()));
                            message.setText(currencyRate.getCharCode() + ": " + currencyRate.getValue());
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }).log()
                    .subscribe();

        } else {
            cbrServiceFeign.getRate(text, date.format(DATE_FORMATTER))
                    .doOnNext(currencyRate -> {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(update.getMessage().getChatId()));
                        message.setText(String.format("%s (%s): %s", currencyRate.getName(), currencyRate.getCharCode(), currencyRate.getValue()));
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    })
                    .log().subscribe();
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
