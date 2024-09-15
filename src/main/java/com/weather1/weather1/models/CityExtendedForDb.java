package com.weather1.weather1.models;

import com.weather1.weather1.repositories.CityInFileRepository1;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "citesExtended")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityExtendedForDb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer numberCharacters;
    private Integer hashCharacters;
    private char firstSymbol;
    private char secondSymbol;
    private char lastSymbol;
    private char preLastSymbol;

    public static void main(String[] args) {
        City city = new City(1L, "Toki");
        CityExtendedForDb cityExtendedForDb = new CityExtendedForDb(city);
        System.out.println();
    }

    public CityExtendedForDb(City city){
        id = city.getId();
        name = city.getName();
        numberCharacters = name.length();
        hashCharacters = getHashCharacters(name);
        firstSymbol = name.charAt(0);
        secondSymbol = name.charAt(1);
        lastSymbol = name.charAt(name.length() - 1);
        preLastSymbol = name.charAt(name.length() - 2);
    }

    public static boolean isEqualsHashWithOneError (int hash1, int hash2){
        return Math.abs(hash1 - hash2) <= 26;
    }
    public static int getHashCharacters(String name) {
        int result = 0;
        name.toLowerCase();
        for (char c : name.toCharArray()) {
            if ('0' <= c && c <= '9') result += c - '0' + 1;
            if ('a' <= c && c <= 'z') result += c - 'a' + 1;
        }
        return result;
    }



}
