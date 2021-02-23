package ru.maxal.abtask.currencyservice.restcontroller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxal.abtask.currencyservice.client.ExchangeratesClient;
import ru.maxal.abtask.currencyservice.client.GifClient;
import ru.maxal.abtask.currencyservice.dto.currency.CurrencyDto;
import ru.maxal.abtask.currencyservice.dto.gif.GifData;
import ru.maxal.abtask.currencyservice.dto.gif.GifDto;
import ru.maxal.abtask.currencyservice.exception.GifNotFoundException;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by maxxii on 20.02.2021.
 */
@RestController
@RequestMapping("/currency/api/v1")
public class CurrencyController {

    @Value("${currency.rate}")
    private String yourCurrency;
    @Value("${gif.limit}")
    private int limitForGIF;
    @Value("${search.gif.good_day}")
    private String goodExchangeRate;
    @Value("${seach.gif.bad_day}")
    private String badExchangeRate;
    @Value("${currency.days_ago}")
    private int daysAgo;

    private final Logger logger = LoggerFactory.getLogger(CurrencyController.class);


    private final ExchangeratesClient currencyClient;
    private final GifClient gifClient;

    public CurrencyController(ExchangeratesClient currencyClient, GifClient gifClient) {
        this.currencyClient = currencyClient;
        this.gifClient = gifClient;
    }


    @GetMapping("/{сurrency}")
    public ResponseEntity<?> usd(@PathVariable("сurrency") String curr) {
        CurrencyDto current = currencyClient.getCurrency(curr);
        CurrencyDto yesterday = currencyClient.yesterday(LocalDate.now().minusDays(daysAgo) + ".json", curr);
        Map<String, Double> todayRates = current.getRates();
        Map<String, Double> yesterdayRates = yesterday.getRates();
        logger.info("Today rates for "+yourCurrency+":"+todayRates.get(yourCurrency));
        logger.info("Yesterday rates for "+yourCurrency+":"+yesterdayRates.get(yourCurrency));
        if (todayRates.get(yourCurrency).compareTo(yesterdayRates.get(yourCurrency)) > 0) {
            return ResponseEntity.ok(randomGif(goodExchangeRate, limitForGIF).getImages().getDownsized());
        } else {
            return ResponseEntity.ok(randomGif(badExchangeRate, limitForGIF).getImages().getDownsized());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(currencyClient.all());
    }


    private GifData randomGif(String name, int limit) {
        GifDto gif = gifClient.getGif(name, limit);
        if (gif.getData().isEmpty()) {
            throw new GifNotFoundException("On request: " + name + " gif not found");
        } else {
            return gif.getData().get((int) (Math.random() * limit));
        }
    }
}
