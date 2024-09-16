package com.weather1.weather1.repositories;

import com.weather1.weather1.models.CityExtendedForDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityExtendedRepository extends JpaRepository<CityExtendedForDb, Long> {
    List<CityExtendedForDb> findAllByName(String name);
    List<CityExtendedForDb> findAllByNumberCharactersBetweenAndHashCharactersBetween(int numberStart, int numberEnd, int hashStart, int hashEnd);
}