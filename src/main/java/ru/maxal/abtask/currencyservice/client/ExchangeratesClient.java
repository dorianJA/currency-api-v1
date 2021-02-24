package ru.maxal.abtask.currencyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxal.abtask.currencyservice.dto.currency.CurrencyDto;

/**
 * Created by maxxii on 20.02.2021.
 */

@FeignClient(url = "${feign.currencyclient.url}",name = "EXC-CLIENT")
public interface ExchangeratesClient {


        @GetMapping("/latest.json?app_id="+"${openexchangerates.app_id}")
        CurrencyDto getCurrency(@PathVariable("base") String currency);

        @GetMapping("/historical/{date}?app_id="+"${openexchangerates.app_id}")
        CurrencyDto currencyFromDate(@PathVariable("date") String date, @RequestParam("base") String currency);

        @GetMapping("//latest.json?app_id="+"${openexchangerates.app_id}")
        CurrencyDto allCurrency();

}
