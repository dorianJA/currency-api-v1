package ru.maxal.abtask.currencyservice.dto.currency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by maxxii on 20.02.2021.
 */
@Getter
@Setter
public class CurrencyDto {
    private String timestamp;
    private String base;
    private Map<String,Double> rates;
}
