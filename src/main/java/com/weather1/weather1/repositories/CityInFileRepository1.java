package com.weather1.weather1.repositories;

import com.weather1.weather1.models.City;
import com.weather1.weather1.service.ErrorsService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for working with cities from a file
 */
@Repository
@Slf4j
@Getter
@Setter
public class CityInFileRepository1 {

    private static final Path path = Paths.get("src/main/resources/static/worldcities.csv");
    private List<City> cities;

    public CityInFileRepository1() {
        this(path);
    }

    public CityInFileRepository1(Path path) {
        try {
            Scanner in = new Scanner(path);
            String[] thead = in.nextLine().split("\"[,\"]*");
            int indexCity = Arrays.asList(thead).indexOf("city_ascii");
            if (indexCity == -1) {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    log.error("Error file is not contains city_ascii", e);
                }

            }
            cities = new ArrayList<>();
            while (in.hasNextLine()) {
                String[] fields = in.nextLine().split("\"[,\"]*");
                cities.add(new City(0L, fields[indexCity]));
            }

        } catch (IOException e) {
            log.error("Error with path file with cites", e);
        }
    }

    /**
     * The method checks for the presence of a city with the specified name in the file
     * @param inputCityName - argument
     * @return - list of all cities with this name
     */

    public List<City> isExistCity(String inputCityName) {
        List<City> result = new ArrayList<>();
        for (City city : cities)
            if (inputCityName.equals(city.getName()))
                result.add(city);
        return result;
    }
    /**
     * The method checks for the presence of a city with the specified name with one error in the file
     * @param inputCityName - argument
     * @return - list of all cities with this name with one error
     */

    public List<City> isExistCityWithErrorName(String inputCityName) {
        List<City> result = isExistCity(inputCityName);
        if (result.size() == 0)
            for (City city : cities)
                if (ErrorsService.isOneError(inputCityName, city.getName()))
                    result.add(city);

        return result;
    }




}


