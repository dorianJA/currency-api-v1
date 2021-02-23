package ru.maxal.abtask.currencyservice.restcontroller.advice;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.maxal.abtask.currencyservice.exception.GifNotFoundException;

/**
 * Created by maxxii on 21.02.2021.
 */
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(FeignException.FeignClientException.class)
    public ResponseEntity<?> notAllowedException(FeignException.FeignClientException client) {
        return ResponseEntity.badRequest().body("This version API only for USD, " +
                "to use the full functionality, change the API key to Developer, " +
                "Enterprise and Unlimited plan or contact support@openexchangerates.org");
    }

    @ExceptionHandler(GifNotFoundException.class)
    public ResponseEntity<?> gifNotFound(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
