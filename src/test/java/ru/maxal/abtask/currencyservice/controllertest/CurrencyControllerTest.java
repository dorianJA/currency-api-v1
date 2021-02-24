package ru.maxal.abtask.currencyservice.controllertest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxal.abtask.currencyservice.client.ExchangeratesClient;
import ru.maxal.abtask.currencyservice.client.GifClient;
import ru.maxal.abtask.currencyservice.dto.currency.CurrencyDto;
import ru.maxal.abtask.currencyservice.dto.gif.GifDto;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by maxxii on 21.02.2021.
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
public class CurrencyControllerTest {

    @Value("${server.port}")
    private int PORT;
    @Value("${gif.limit.test}")
    private int limitForGIf;


    @Autowired
    private MockMvc mockMvc;

    private final GifClient gifClient;
    private final ExchangeratesClient exchangeratesClient;

    @Autowired
    public CurrencyControllerTest(GifClient gifClient, ExchangeratesClient exchangeratesClient) {
        this.gifClient = gifClient;
        this.exchangeratesClient = exchangeratesClient;
    }


    @Test
    public void gifTesting() throws Exception {

        GifDto gif = gifClient.getGif("broke", limitForGIf);

        assertNotNull(gif);
        assertFalse(gif.getData().isEmpty());
        assertEquals(limitForGIf, gif.getData().size());

        mockMvc.perform(get("/currency/api/v1/USD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void allExchangerates() throws Exception {
        CurrencyDto all = exchangeratesClient.all();
        Map<String, Double> rates = all.getRates();
        assertFalse(rates.isEmpty());

        mockMvc.perform(get("/currency/api/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rates").value(rates));
    }

    @Test
    public void getUSD() throws Exception {
        CurrencyDto usd = exchangeratesClient.getCurrency("USD");
        assertEquals("USD", usd.getBase());

        mockMvc.perform(get("/currency/api/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.base").value(usd.getBase()));

    }


}
