package com.weather1.weather1.repositories;

import com.weather1.weather1.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByName(String name);
    List<City> findAllByNameIsIn(List<String> names);

}
