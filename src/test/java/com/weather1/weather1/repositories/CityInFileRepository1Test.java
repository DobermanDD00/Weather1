package com.weather1.weather1.repositories;

import com.weather1.weather1.models.City;
import com.weather1.weather1.service.ErrorsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CityInFileRepository1Test {

    CityInFileRepository1 repo = new CityInFileRepository1();


    /**
     * test with use random values
     */
    @Test
    void isExistCity() {
        int size = repo.getCities().size();
        assertTrue(size > 10000);
        assertEquals("Tokyo", repo.isExistCity("Tokyo").get(0).getName());
        assertEquals("Moscow", repo.isExistCity("Moscow").get(0).getName());
        assertEquals("Charlotte Amalie", repo.isExistCity("Charlotte Amalie").get(0).getName());
        assertTrue(repo.isExistCity("Tokio").size() == 0);


        for (int i = 0; i < 100; i++) {
            int position = (int) (Math.random() * size);
            String name = repo.getCities().get(position).getName();
            List<City> cities = repo.isExistCity(name);
            for (City city : cities)
                assertEquals(name, city.getName());
        }

    }

    /**
     * test with use random values
     */
    @Test
    void isExistCityWithErrorName() {
        int size = repo.getCities().size();
        assertTrue(size > 10000);
        assertEquals("Tokyo", repo.isExistCityWithErrorName("Tokyo").get(0).getName());
        assertEquals("Moscow", repo.isExistCityWithErrorName("Moscow").get(0).getName());
        assertEquals("Charlotte Amalie", repo.isExistCityWithErrorName("Charlotte Amalie").get(0).getName());
        assertEquals("Tokyo", repo.isExistCityWithErrorName("Tokio").get(0).getName());


        assertEquals("Tokyo", repo.isExistCityWithErrorName("Toko").get(0).getName());
        assertEquals("Moscow", repo.isExistCityWithErrorName("Moskow").get(0).getName());
        assertEquals("Charlotte Amalie", repo.isExistCityWithErrorName("Charlotdte Amalie").get(0).getName());
        assertEquals("Tokyo", repo.isExistCityWithErrorName("Tykoo").get(0).getName());

        //check correct find correct names

        for (int i = 0; i < 100; i++) {
            int position = (int) (Math.random() * size);
            String name = repo.getCities().get(position).getName();
            List<City> cities = repo.isExistCityWithErrorName(name);
            for (City city : cities)
                if (!ErrorsService.isOneErrorOrLess(name, city.getName())) {
                    log.error("expected = {}, actual = {}", name, city.getName());

                    assertTrue(false);
                }

        }
        //check correct find error names
        for (int i = 0; i < 100; i++) {
            int position = (int) (Math.random() * size);
            String name = repo.getCities().get(position).getName();
            List<String> errorNames = ErrorsService.generateAllWithOneError(name);
            List<City> cities = repo.isExistCityWithErrorName(errorNames.get((int) (Math.random() * errorNames.size())));
            boolean flagExistCorrect = false;
            for (City city : cities)
                if (ErrorsService.isOneErrorOrLess(name, city.getName())) {
                    flagExistCorrect = true;
                }
            if (!flagExistCorrect) {
                log.error("no founded {}, but its exist", name);
                assertTrue(false);
            }
        }
    }
}