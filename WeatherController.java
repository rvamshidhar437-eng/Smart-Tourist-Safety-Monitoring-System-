package com.touristsafety.controller;

import com.touristsafety.dto.WeatherResponse;
import com.touristsafety.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public WeatherResponse current(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.currentWeather(latitude, longitude);
    }
}
