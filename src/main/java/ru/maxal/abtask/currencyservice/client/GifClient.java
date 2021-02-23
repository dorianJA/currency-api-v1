package ru.maxal.abtask.currencyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxal.abtask.currencyservice.dto.gif.GifDto;

/**
 * Created by maxxii on 20.02.2021.
 */
@FeignClient(url = "${feign.gif.url}", name = "GIF-CLIENT")
public interface GifClient {

    @GetMapping("/search?api_key="+"${giphy.api_key}")
    GifDto getGif(@RequestParam("q") String search,@RequestParam("limit") int limit);



}
