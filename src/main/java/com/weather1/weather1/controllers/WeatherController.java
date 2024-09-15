package com.weather1.weather1.controllers;

import com.weather1.weather1.models.Weather.Weather;
import com.weather1.weather1.repositories.CityInFileRepository;
import com.weather1.weather1.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final CityService cityService;

    @PostMapping("/weather")
    public String showWeather(@RequestParam String city, Model model) {

        CityInFileRepository cityInFileRepository = null;
        try {
            cityInFileRepository = new CityInFileRepository();
        } catch (IOException e) {
            System.out.println();
            log.warn("Error with cityInFileRepository", e);
        }

        String cityCorrect = cityInFileRepository.isExistCityWithErrorName(city);
        String reportCity = "The results for the request " + city + " are given";
        if (!city.equals(cityCorrect))
            reportCity = "The entered city " + city + " is missing, the results for the city " + cityCorrect + " are given";

        RestTemplate restTemplate = new RestTemplate();
        Weather weather = restTemplate.getForObject("http://api.weatherapi.com/v1/current.json?key=32e079e0ae0e4e908e4195940240809&q=" + cityCorrect + "&aqi=no", Weather.class);

        model.addAttribute("reportCity", reportCity);
        model.addAttribute(weather);
        return "weather";

    }

    @GetMapping("/init-bd")
    public String products(Model model) {
        model.addAttribute("report", cityService.initDb());
        return "test-bd";
    }



}