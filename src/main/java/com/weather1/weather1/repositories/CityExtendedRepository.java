package com.weather1.weather1.repositories;

import com.weather1.weather1.models.CityExtendedForDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityExtendedRepository extends JpaRepository<CityExtendedForDb, Long> {

}