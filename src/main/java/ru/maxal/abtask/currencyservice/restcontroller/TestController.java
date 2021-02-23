package ru.maxal.abtask.currencyservice.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxal.abtask.currencyservice.client.ExchangeratesClient;
import ru.maxal.abtask.currencyservice.client.GifClient;
import ru.maxal.abtask.currencyservice.dto.currency.CurrencyDto;

/**
 * Created by maxxii on 20.02.2021.
 */
@Controller
public class TestController {

    @Autowired
    private GifClient gifClient;
    @Autowired
    private ExchangeratesClient exchangeratesClient;

    @GetMapping("/")
    public String page(ModelMap modelMap) {
        String rich = gifClient.getGif("rich", 10).getData()
                .get((int) (Math.random() * 10)).getImages().getDownsized().getUrl();
        CurrencyDto usd = exchangeratesClient.getCurrency("USD");
        modelMap.addAttribute("rate", usd.getRates().get("RUB"));
        modelMap.addAttribute("gif", rich);
        return "index";
    }
}
