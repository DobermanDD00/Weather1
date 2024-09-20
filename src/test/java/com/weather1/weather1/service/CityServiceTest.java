package com.weather1.weather1.service;

import com.weather1.weather1.models.City;
import com.weather1.weather1.repositories.CityExtendedRepository;
import com.weather1.weather1.repositories.CityInFileRepository1;
import com.weather1.weather1.repositories.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityServiceTest {

    @Autowired
    CityService service;
    /**
     * run on with cites file
     */

    @Test
    void findCityWithOneErrorInFile() {
        CityInFileRepository1 cityInFileRepository1 = new CityInFileRepository1();
        String name = "Tokyo";
        List<City> cities = service.findCityWithOneErrorInFile(name);
        if (cities.size() == 0) assertTrue(false);
        for (City city : cities) {
            assertTrue(ErrorsService.isOneErrorOrLess(city.getName(), name));
        }
        name = "Moskow";
        cities = service.findCityWithOneErrorInFile(name);
        if (cities.size() == 0) assertTrue(false);
        for (City city : cities) {
            assertTrue(ErrorsService.isOneErrorOrLess(city.getName(), name));
        }
        name = "wkeruh";
        cities = service.findCityWithOneErrorInFile(name);
        if (cities.size() == 0) assertFalse(false);
        for (City city : cities) {
            assertTrue(ErrorsService.isOneErrorOrLess(city.getName(), name));
        }


    }

    /**
     * run on an initialized database
     */
    @Test
    void findCityWithOneErrorInDbWithGenerationCitesSet() {
        String planeName = "Tokyo";
        List<City> cites = service.findCityWithOneErrorInDbWithGenerationCitesSet(planeName);
        assertTrue(cites.size() >= 1);
        assertEquals(planeName, cites.get(0).getName());


        planeName = "Toko";
        cites = service.findCityWithOneErrorInDbWithGenerationCitesSet(planeName);
        assertTrue(cites.size() > 1);
        for (City city : cites) {
            assertFalse(city.getName().equals(""));
            assertTrue(ErrorsService.isOneErrorOrLess(city.getName(), planeName));
        }

    }

    /**
     * run on an initialized database
     */
    @Test
    void findCityWithOneErrorInDbWithHash() {
        String planeName = "Tokyo";
        List<City> cites = service.findCityWithOneErrorInDbWithHash(planeName);
        assertTrue(cites.size() >= 1);
        assertEquals(planeName, cites.get(0).getName());

        planeName = "Toko";
        cites = service.findCityWithOneErrorInDbWithHash(planeName);
        assertTrue(cites.size() > 1);
        for (City city : cites) {
            assertFalse(city.getName().equals(""));
            assertTrue(ErrorsService.isOneErrorOrLess(city.getName(), planeName));
        }
    }


}