package com.weather1.weather1.service;

import com.weather1.weather1.models.City;
import com.weather1.weather1.repositories.CityInFileRepository;
import com.weather1.weather1.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> listCites(String name) {
        if (name != null) return cityRepository.findByName(name);
        return cityRepository.findAll();
    }

    public String initDb() {
        try {
            CityInFileRepository cityInFileRepo = new CityInFileRepository();
            for(String str: cityInFileRepo.getStrs()){
                City city = new City(0L, cityInFileRepo.getCityName(str));
                cityRepository.save(city);
            }


        } catch (IOException e) {
            log.error("Error with creation {}", CityInFileRepository.class, e);
        }


        return "testReport";
    }

    public void saveCity(City city) {
        log.info("Saving new {} {}", city.getName(), city);
        cityRepository.save(city);
    }



}