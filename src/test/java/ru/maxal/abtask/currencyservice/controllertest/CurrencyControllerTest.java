package ru.maxal.abtask.currencyservice.controllertest;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxal.abtask.currencyservice.client.ExchangeratesClient;
import ru.maxal.abtask.currencyservice.client.GifClient;
import ru.maxal.abtask.currencyservice.dto.currency.CurrencyDto;
import ru.maxal.abtask.currencyservice.dto.gif.GifDto;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Created by maxxii on 21.02.2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
public class CurrencyControllerTest {

    @Value("${server.port}")
    private int PORT;
    @Value("${gif.limit.test}")
    private int limitForGIf;


    @Autowired
    private GifClient gifClient;
    @Autowired
    private ExchangeratesClient exchangeratesClient;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(PORT));

    @Test
    public void gifTesting() {
        wireMockRule.stubFor(get(urlPathMatching("/currency/api/v1/USD"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")));



        GifDto gif = gifClient.getGif("broke", limitForGIf);
        assertFalse(gif.getData().isEmpty());
        Assertions.assertEquals(limitForGIf, gif.getData().size());



    }

    @Test
    public void allExchangerates() {
        CurrencyDto all = exchangeratesClient.all();
        Map<String, Double> rates = all.getRates();
        assertFalse(rates.isEmpty());
    }

    @Test
    public void getUSD() {
        CurrencyDto usd = exchangeratesClient.getCurrency("USD");
        assertEquals("USD", usd.getBase());
    }


}
