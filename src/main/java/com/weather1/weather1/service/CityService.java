package com.weather1.weather1.service;

import com.weather1.weather1.models.City;
import com.weather1.weather1.models.CityExtendedForDb;
import com.weather1.weather1.repositories.CityExtendedRepository;
import com.weather1.weather1.repositories.CityInFileRepository1;
import com.weather1.weather1.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
/**
 * A class for searching for the names of cities with an error in various repositories
 */
public class CityService {
    private final CityInFileRepository1 cityInFileRepository1;
    private final CityRepository cityRepository;
    private final CityExtendedRepository cityExtendedRepository;

    /**
     * Method for initialize db cites
     * @return "Db cites is initialized"
     */
    public String initDbCites() {
        CityInFileRepository1 cityInFileRepo1 = new CityInFileRepository1();
        cityRepository.saveAll(cityInFileRepo1.getCities());
        return "Db cites is initialized";
    }
    /**
     * Method for initialize db cites extended
     * @return "Db cites extended is initialized"
     */
    public String initDbCityExtended() {
        CityInFileRepository1 cityInFileRepo1 = new CityInFileRepository1();
        List<CityExtendedForDb> cityExtendedForDbs = cityInFileRepo1.getCities().stream().map(CityExtendedForDb::new).collect(Collectors.toList());
        cityExtendedRepository.saveAll(cityExtendedForDbs);
        return "Db cites extended is initialized";
    }

    /**
     * The method of searching for a city by name with an error in the file
     * @param name - name of city
     * @return - list of cites
     */
    public List<City> findCityWithOneErrorInFile(String name) {
        long timeMillis = System.currentTimeMillis();
        List<City> result = cityInFileRepository1.isExistCityWithErrorName(name);
        log.info("Time find city in file: {} millis", System.currentTimeMillis() - timeMillis);
        return result;
    }
    /**
     * The method of searching for a city by name with an error in the in db cites by generating all possible names with errors
     * @param name - name of city
     * @return - list of cites
     */
    public List<City> findCityWithOneErrorInDbWithGenerationCitesSet(String name) {
        long timeMillis = System.currentTimeMillis();
        List<City> cities = cityRepository.findByName(name);
        if (cities.size() == 0) {
            List<String> names = ErrorsService.generateAllWithOneError(name);
            cities = names.stream().flatMap(p -> (cityRepository.findByName(p).stream())).collect(Collectors.toList());
        }
        log.info("Time find city in Db with generation set: {} millis", System.currentTimeMillis() - timeMillis);
        return cities;
    }
    /**
     * The method of searching for a city by name with an error in the in db cites extended by hash values
     * @param name - name of city
     * @return - list of cites
     */
    public List<City> findCityWithOneErrorInDbWithHash(String name) {
        long timeMillis = System.currentTimeMillis();
        List<City> cities = new ArrayList<>();
        List<CityExtendedForDb> cityExtendedForDbs = cityExtendedRepository.findAllByName(name);
        if (cityExtendedForDbs.size() >= 1) {
            cities = cityExtendedForDbs.stream().map(p->new City(p.getId(), p.getName())).collect(Collectors.toList());
        } else {
            int numberStart = name.length() - 1;
            int numberEnd = name.length() + 1;
            int hashStart = CityExtendedForDb.getHashCharacters(name) - CityExtendedForDb.hashDifference;
            int hashEnd = CityExtendedForDb.getHashCharacters(name) + CityExtendedForDb.hashDifference;
            cityExtendedForDbs = cityExtendedRepository.findAllByNumberCharactersBetweenAndHashCharactersBetween(numberStart, numberEnd, hashStart, hashEnd);
            for (CityExtendedForDb cityExtendedForDb : cityExtendedForDbs)
                if (ErrorsService.isOneError(name, cityExtendedForDb.getName()))
                    cities.add(new City(cityExtendedForDb.getId(), cityExtendedForDb.getName()));
        }

        log.info("Time find city in Db with hash: {} millis", System.currentTimeMillis() - timeMillis);
        return cities;
    }


}