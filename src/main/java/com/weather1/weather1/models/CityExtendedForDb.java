package com.weather1.weather1.models;

import com.weather1.weather1.repositories.CityInFileRepository1;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class for storing cities with additional hash values to speed up the search with errors
 */
@Entity
@Table(name = "citesExtended")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityExtendedForDb {
    public static final int hashDifference = 26;

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

    /**
     *A method for checking that the hashes of 2 city names differ by no more than one error. There are "false positive" positives. There are no "false negatives".
     * @param hash1 - argument 1
     * @param hash2 - argument 2
     * @return - is 2 city names differ by no more than one error. There are "false positive" positives. There are no "false negatives".
     */
    public static boolean isEqualsHashWithOneError (int hash1, int hash2){
        return Math.abs(hash1 - hash2) <= 26;
    }

    /**
     * A method for getting a hash value by name
     * @param name - argument
     * @return - hash
     */
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
