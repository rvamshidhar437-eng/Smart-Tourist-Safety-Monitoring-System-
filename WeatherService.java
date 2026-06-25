package com.touristsafety.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.touristsafety.dto.WeatherResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public WeatherService(RestTemplate restTemplate, @Value("${app.open-weather.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public WeatherResponse currentWeather(double latitude, double longitude) {
        if (apiKey == null || apiKey.isBlank()) {
            return new WeatherResponse(
                    null,
                    "Weather API key is not configured.",
                    false,
                    List.of("Set OPENWEATHER_API_KEY to enable live weather.")
            );
        }

        URI uri = UriComponentsBuilder.fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build()
                .toUri();

        try {
            JsonNode response = restTemplate.getForObject(uri, JsonNode.class);
            if (response == null) {
                return unavailable();
            }
            double temperature = response.path("main").path("temp").asDouble();
            String summary = response.path("weather").isArray() && response.path("weather").size() > 0
                    ? response.path("weather").get(0).path("description").asText("Weather available")
                    : "Weather available";
            boolean rain = response.has("rain") || summary.toLowerCase().contains("rain");
            List<String> alerts = new ArrayList<>();
            if (rain) {
                alerts.add("Rain expected. Carry waterproof gear and avoid flood-prone routes.");
            }
            if (temperature >= 38) {
                alerts.add("High temperature. Hydrate frequently and avoid long exposure.");
            }
            if (alerts.isEmpty()) {
                alerts.add("No severe weather alerts for the selected area.");
            }
            return new WeatherResponse(temperature, summary, rain, alerts);
        } catch (RestClientException ex) {
            return unavailable();
        }
    }

    private WeatherResponse unavailable() {
        return new WeatherResponse(null, "Weather service unavailable.", false, List.of("Try again later."));
    }
}
