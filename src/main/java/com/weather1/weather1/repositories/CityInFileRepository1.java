package com.weather1.weather1.repositories;

import com.weather1.weather1.models.City;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Getter
@Setter
public class CityInFileRepository1 {
    public static void main(String[] args) {
        CityInFileRepository1 cityInFileRepository1 = new CityInFileRepository1();
        System.out.println(cityInFileRepository1.isExistCityWithErrorName("Moscow"));
        System.out.println(cityInFileRepository1.isExistCityWithErrorName("Moskow"));
        System.out.println(cityInFileRepository1.isExistCityWithErrorName("Mosko"));
    }
    private static final Path path = Paths.get("src/main/resources/static/worldcities.csv");
    private Map <String, City> cities;
    public CityInFileRepository1(){
        this(path);
    }
    public CityInFileRepository1(Path path){
        try {
            Scanner in = new Scanner(path);
            String[] thead = in.nextLine().split("\"[,\"]*");
            int indexCity = Arrays.asList(thead).indexOf("city_ascii");
            if (indexCity == -1){
                try{
                    throw new IOException();
                 }catch(IOException e){
                    log.error("Error file is not contains city_ascii",e);
                }

            }
            cities = new HashMap<>();
            while (in.hasNextLine()){
                String[] fields = in.nextLine().split("\"[,\"]*");
                cities.put( fields[indexCity], new City(0L, fields[indexCity]));
            }

        } catch (IOException e) {
            log.error("Error with path file with cites", e);
        }
    }



    public String isExistCityWithErrorName(String inputCityName) {
        long timeMillis = System.currentTimeMillis();
        for (String str: cities.keySet()){
            if (isOneError(inputCityName, str)) {
                log.info("Time find city in Map: {} millis", System.currentTimeMillis() - timeMillis);
                return str;
            }
        }
        log.info("Time find city in Map: {} millis", System.currentTimeMillis() - timeMillis);
        return null;
    }
    public boolean isOneError(String name, String planeName) {
        if (isMissingSymbol(name, planeName)) return true;
        if (isRedundantSymbol(name, planeName)) return true;
        if (isWrongSymbol(name, planeName)) return true;
        if (isMisplacedSymbol(name, planeName)) return true;
        return false;

    }

    public boolean isMissingSymbol(String name, String planeName) {
        if (name.length() != planeName.length() - 1) return false;
        int error = 0;
        for (int i = 0; i < name.length(); ) {
            if (name.charAt(i) == planeName.charAt(i + error)) {
                i++;
            } else {
                if (error == 0) error++;
                else
                    return false;
            }
        }
        return true;

    }

    public boolean isRedundantSymbol(String name, String planeName) {
        return isMissingSymbol(planeName, name);
    }

    public boolean isWrongSymbol(String name, String planeName) {
        if (name.length() != planeName.length()) return false;
        int errors = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != planeName.charAt(i)) errors++;
            if (errors > 1) return false;
        }
        return true;

    }

    public boolean isMisplacedSymbol(String name, String planeName) {
        if (name.length() != planeName.length()) return false;
        int[] indexesMisplaced = new int[2];
        int errors = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != planeName.charAt(i)) {
                if (errors > 1) return false;
                indexesMisplaced[errors] = i;
                errors++;
            }
        }

        return name.charAt(indexesMisplaced[0]) == planeName.charAt(indexesMisplaced[1])
                && name.charAt(indexesMisplaced[1]) == planeName.charAt(indexesMisplaced[0]);

    }



}
