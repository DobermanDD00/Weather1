package com.weather1.weather1.repositories;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public class CityInFileRepository {
    public static void main(String[] args) {
        CityInFileRepository cityInFileRepository  = null;
        try {
            cityInFileRepository = new CityInFileRepository();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(cityInFileRepository.isMissingSymbol("Mosko", "Moskow"));
        System.out.println(cityInFileRepository.isMissingSymbol("Toky", "Tokyo"));
        System.out.println(cityInFileRepository.isExistCityWithErrorName("Mosko"));
        System.out.println(cityInFileRepository.isExistCityWithErrorName("Toky"));

    }

    //    "city","city_ascii","lat","lng","country","iso2","iso3","admin_name","capital","population","id"

    private Path path;
    private List<String> strs;
    private String lineHeaders;


    public CityInFileRepository() throws IOException {
        this("src/main/resources/static/worldcities.csv");
    }

    public CityInFileRepository(String path) throws IOException {
        this(Paths.get(path));
    }


    public CityInFileRepository(Path path) throws IOException {
        this.path = path;
        strs = Files.readAllLines(path);
        lineHeaders = strs.get(0);
        strs.remove(0);
    }


    public String isExistCityWithErrorName(String inputCityName) {
        long timeMillis = System.currentTimeMillis();

        for (String str : strs) {
            String cityName = getCityName(str);
            if (inputCityName.equals(cityName)) {
                log.info("Time find city in Map: {} millis", System.currentTimeMillis() - timeMillis);
                return inputCityName;
            }
            else if (isOneError(inputCityName, cityName)) {
                log.info("Time find city in Map: {} millis", System.currentTimeMillis() - timeMillis);
                return cityName;
            }
        }
        return null;
    }
    public String getCityName(String str){
        List<Integer> indexesQuotationMarks = indexesQuotationMarks(str);
        return  str.substring(indexesQuotationMarks.get(2) + 1, indexesQuotationMarks.get(3));
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

        if (name.charAt(indexesMisplaced[0]) == planeName.charAt(indexesMisplaced[1])
                && name.charAt(indexesMisplaced[1]) == planeName.charAt(indexesMisplaced[0])) return true;
        else return false;

    }

    public List<Integer> indexesQuotationMarks(String str) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '"') indexes.add(i);
        }
        return indexes;
    }


}
