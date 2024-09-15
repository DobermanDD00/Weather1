package com.weather1.weather1.controllers;

import com.weather1.weather1.models.City;
import com.weather1.weather1.models.Weather.Weather;
import com.weather1.weather1.repositories.CityInFileRepository;
import com.weather1.weather1.repositories.CityInFileRepository1;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final CityService cityService;

    @GetMapping("/weather")
    public String taskInfo() {

        return "weather";

    }

    @PostMapping("/weather")
    public String showWeather(@RequestParam String city, Model model) {
        // File search with fullTableScan and Map
//        CityInFileRepository1 cityInFileRepository = new CityInFileRepository1();
//        String cityCorrect = cityInFileRepository.isExistCityWithErrorName(city);

        // File search with fullTableScan
//        CityInFileRepository cityInFileRepository = null;
//        try {
//            cityInFileRepository = new CityInFileRepository();
//        } catch (IOException e) {
//            System.out.println();
//            log.warn("Error with cityInFileRepository", e);
//        }
//        String cityCorrect = cityInFileRepository.isExistCityWithErrorName(city);

        //Find in Db with generated names
        List<City> cities = cityService.findCityWithOneError(city);
        System.out.println();


        String reportCity;
        if (cities.size() == 1)
            reportCity = "The results for the request " + city + " are given";
        else
            reportCity = "The results for: " + cities.stream().map(p -> p.getName()).collect(Collectors.toList()).toString();


        RestTemplate restTemplate = new RestTemplate();
        List<Weather> weathers = new ArrayList<>();
        for (City city1 : cities)
            weathers.add(restTemplate.getForObject("http://api.weatherapi.com/v1/current.json?key=32e079e0ae0e4e908e4195940240809&q=" + city1 + "&aqi=no", Weather.class));

        model.addAttribute("reportCity", reportCity);
        model.addAttribute("weathers",weathers);
        return "weathers";
    }

//    @PostMapping("/weather")
//    public String showWeather(@RequestParam String city, Model model) {
//        // File search with fullTableScan and Map
////        CityInFileRepository1 cityInFileRepository = new CityInFileRepository1();
////        String cityCorrect = cityInFileRepository.isExistCityWithErrorName(city);
//
//        // File search with fullTableScan
////        CityInFileRepository cityInFileRepository = null;
////        try {
////            cityInFileRepository = new CityInFileRepository();
////        } catch (IOException e) {
////            System.out.println();
////            log.warn("Error with cityInFileRepository", e);
////        }
////        String cityCorrect = cityInFileRepository.isExistCityWithErrorName(city);
//
//        //Find in Db with generated names
//        List<City> cities = cityService.findCityWithOneError(city);
//        System.out.println();
//        String cityCorrect = ""; // Delete *********************
//
//
//        String reportCity;
//        if (city.equals(cityCorrect))
//            reportCity = "The results for the request " + city + " are given";
//        else
//            reportCity = "The entered city " + city + " is missing, the results for the city " + cityCorrect + " are given";
//
//        RestTemplate restTemplate = new RestTemplate();
//        Weather weather = restTemplate.getForObject("http://api.weatherapi.com/v1/current.json?key=32e079e0ae0e4e908e4195940240809&q=" + cityCorrect + "&aqi=no", Weather.class);
//
//        model.addAttribute("reportCity", reportCity);
//        model.addAttribute(weather);
//        return "weather";
//
//    }

    @GetMapping("/init-db1")
    public String products1(Model model) {
        long timeMillis = System.currentTimeMillis();

        // initialize City for Db
        model.addAttribute("report", cityService.initDb1());

        // initialize CityExtended for Db
//        model.addAttribute("report", cityService.initDb2());

        long difTime = System.currentTimeMillis() - timeMillis;
        log.info("Time initialize Db: {} millis", difTime);
        return "initializeDb1";
    }

    @GetMapping("/init-db2")
    public String products2(Model model) {
        long timeMillis = System.currentTimeMillis();

        model.addAttribute("report", cityService.initDb2());

        long difTime = System.currentTimeMillis() - timeMillis;
        log.info("Time initialize Db: {} millis", difTime);
        return "initializeDb2";
    }


}