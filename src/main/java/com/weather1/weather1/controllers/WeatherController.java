package com.weather1.weather1.controllers;

import com.weather1.weather1.models.City;
import com.weather1.weather1.models.Weather.Weather;
import com.weather1.weather1.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WeatherController {
    public static final int SEARCH_IN_FILE = 1;
    public static final int SEARCH_IN_DB_WITH_GENERATION_CITES_SET = 2;
    public static final int SEARCH_IN_DB_WITH_HASH = 3;

    private final CityService cityService;

    /**
     * Get the main page
     * @return main page
     */
    @GetMapping("/weather")
    public String showWeather() {
        return "weathers";
    }

    /**
     * To get the weather in the city, the name of the city may contain 1 error. If several cities correspond to the entered name with an error, then all of them will be output. If there is no such city in the weather service, then the weather for it will not be displayed.
     * @param city - the city
     * @param optionSearch - option to select the search method. The value of the available options with a description is specified in the class constants.
     * @param model - argument model
     * @return - weather information in the selected city(s)
     */

    @PostMapping("/weather")
    public String showWeathersInCites(@RequestParam String city, @RequestParam int optionSearch, Model model) {

        List<City> cities = new ArrayList<>();

        switch (optionSearch) {
            case (SEARCH_IN_FILE): {
                cities = cityService.findCityWithOneErrorInFile(city);
                break;
            }
            case (SEARCH_IN_DB_WITH_GENERATION_CITES_SET): {
                cities = cityService.findCityWithOneErrorInDbWithGenerationCitesSet(city);
                break;
            }
            case (SEARCH_IN_DB_WITH_HASH): {
                cities = cityService.findCityWithOneErrorInDbWithHash(city);
                break;
            }
        }


        String reportCity;
        if (cities.size() == 1)
            reportCity = "The results for the request " + city + " are given";
        else
            reportCity = "The results for: " + cities.stream().map(p -> p.getName()).collect(Collectors.toList());


        RestTemplate restTemplate = new RestTemplate();
        List<Weather> weathers = new ArrayList<>();
        for (City city1 : cities) {
            try {
                weathers.add(restTemplate.getForObject("http://api.weatherapi.com/v1/current.json?key=32e079e0ae0e4e908e4195940240809&q=" + city1.getName() + "&aqi=no", Weather.class));
            } catch (RestClientException e) {
                log.info("Information for {} is not founded", city1.getName());
            }
        }

        model.addAttribute("reportCity", reportCity);
        model.addAttribute("weathers", weathers);
        return "weathers";
    }

    /**
     * Initialized db cites
     * @param model - argument model
     * @return "initializeDb1"
     */
    @GetMapping("/init-db-cites")
    public String initializeDbCites(Model model) {
        long timeMillis = System.currentTimeMillis();
        model.addAttribute("report", cityService.initDbCites());
        log.info("Time initialize Db: {} millis", System.currentTimeMillis() - timeMillis);
        return "initializeDb1";
    }
    /**
     * Initialized db cites extended
     * @param model - argument model
     * @return "initializeDb2"
     */
    @GetMapping("/init-db-cites-extended")
    public String initializeDbCitesExtended(Model model) {
        long timeMillis = System.currentTimeMillis();
        model.addAttribute("report", cityService.initDbCityExtended());
        log.info("Time initialize Db: {} millis", System.currentTimeMillis() - timeMillis);
        return "initializeDb2";
    }


}