package com.portfolio.pushpendra.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("api")
public class QuoteController {

    // In-memory quotes
    private List<String> quotes = List.of(
            "Push yourself, because no one else is going to do it for you.",
            "Success is not final; failure is not fatal: It is the courage to continue that counts.",
            "Dream it. Wish it. Do it.",
            "Do something today that your future self will thank you for.",
            "Great things never come from comfort zones."
    );

    @GetMapping("/quote")
    public Map<String, String> getQuote(@RequestHeader("API-Key") String apiKey) {
        // API key validation
        if (!"MY_SECRET_KEY".equals(apiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        // Return random quote
        Random rand = new Random();
        String quote = quotes.get(rand.nextInt(quotes.size()));
        return Map.of("quote", quote);
    }
}
