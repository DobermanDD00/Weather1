package com.weather1.weather1.service;

import com.weather1.weather1.models.City;
import com.weather1.weather1.models.CityExtendedForDb;
import com.weather1.weather1.repositories.CityExtendedRepository;
import com.weather1.weather1.repositories.CityInFileRepository;
import com.weather1.weather1.repositories.CityInFileRepository1;
import com.weather1.weather1.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor

public class CityService {
    private final CityRepository cityRepository;
    private final CityExtendedRepository cityExtendedRepository;

    public static void main(String[] args) {
//        List<String> list = generateCitiesWithOneError("1234567890");
//        System.out.println();
    }

    public List<City> listCites(String name) {
        if (name != null) return cityRepository.findByName(name);
        return cityRepository.findAll();
    }

    public String initDb() {
        try {
            CityInFileRepository cityInFileRepo = new CityInFileRepository();
            for (String str : cityInFileRepo.getStrs()) {
                City city = new City(0L, cityInFileRepo.getCityName(str));
                cityRepository.save(city);
            }


        } catch (IOException e) {
            log.error("Error with creation {}", CityInFileRepository.class, e);
        }


        return "testReport";
    }

    public String initDb1() {
        CityInFileRepository1 cityInFileRepo1 = new CityInFileRepository1();
        cityRepository.saveAll(cityInFileRepo1.getCities().values());

        return "Db is initialized";
    }

    public String initDb2() {
        CityInFileRepository1 cityInFileRepo1 = new CityInFileRepository1();
        List<CityExtendedForDb> cityExtendedForDbs = cityInFileRepo1.getCities().values().stream().map(CityExtendedForDb::new).collect(Collectors.toList());
        cityExtendedRepository.saveAll(cityExtendedForDbs);
        return "Db is initialized";
    }


    public void saveCity(City city) {
        log.info("Saving new {} {}", city.getName(), city);
        cityRepository.save(city);
    }

    public List<String> generateCitiesWithOneError(String name) {
        List<String> result = new ArrayList<>();

        //Missing symbol
        for (int i = 0; i < name.length(); i++) {
            result.add(name.substring(0, i) + name.substring(i + 1, name.length()));
        }
        //Redundant symbol
        for (int i = 0; i <= name.length(); i++)
            for (int j = 0; j < 26; j++) {
                char c = (char) ('a' + j);
                result.add(name.substring(0, i) + c + name.substring(i, name.length()));
            }
        //Wrong symbol
        for (int i = 0; i < name.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char c = (char) ('a' + j);
                if (name.charAt(i) != c)
                    result.add(name.substring(0, i) + c + name.substring(i + 1, name.length()));
            }
        }
        //Misplaced symbol
        for (int i = 0; i < name.length(); i++) {
            for (int j = i + 1; j < name.length(); j++) {
                char[] word = name.toCharArray();
                char c = word[i];
                word[i] = word[j];
                word[j] = c;
                result.add(new String(word));

            }
        }

        return result;
    }

    public List<City> findCityWithOneError(String name) {
        // Made if no error
        List<String> names = generateCitiesWithOneError(name);
        List<City> cities = names.stream().flatMap(p -> (cityRepository.findByName(p).stream())).collect(Collectors.toList());

        return cities;

    }

}