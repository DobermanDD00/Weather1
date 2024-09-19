package com.weather1.weather1.service;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class with static methods to check that words differ by one error.
 */
public class ErrorsService {

    /**
     * The method of generating all words that differ from the original one by 1 error
     * @param name - the original word
     * @return - list of results
     */
    public static List<String> generateAllWithOneError(String name) {
        List<String> result = new ArrayList<>();

        //Missing symbol
        for (int i = 0; i < name.length(); i++) {
            result.add(name.substring(0, i) + name.substring(i + 1, name.length()));
        }
        //Redundant symbol
        for (int i = 0; i <= name.length(); i++)
            for (int j = 0; j < 26; j++) {
                char c = (char) ('a' + j);
                result.add(name.substring(0, i) + c + name.substring(i, name.length()));
            }
        //Wrong symbol
        for (int i = 0; i < name.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char c = (char) ('a' + j);
                if (name.charAt(i) != c)
                    result.add(name.substring(0, i) + c + name.substring(i + 1, name.length()));
            }
        }
        //Misplaced symbol
        for (int i = 0; i < name.length(); i++) {
            for (int j = i + 1; j < name.length(); j++) {
                char[] word = name.toCharArray();
                char c = word[i];
                word[i] = word[j];
                word[j] = c;
                result.add(new String(word));

            }
        }

        return result;
    }

    /**
     * The method for checking the difference of words less than 1 error
     * @param name1 - first word
     * @param name2 - second word
     * @return - the difference is not more than 1 error
     */
    public static boolean isOneErrorOrLess(String name1, String name2) {
        if (name1.equals(name2)) return true;
        if (isMissingSymbol(name1, name2)) return true;
        if (isRedundantSymbol(name1, name2)) return true;
        if (isWrongSymbol(name1, name2)) return true;
        if (isMisplacedSymbol(name1, name2)) return true;
        return false;

    }

    /**
     * The method of checking for a missing character
     * @param name1 - first word
     * @param name2 - second word
     * @return - the difference is no more than one missing character
     */
    public static boolean isMissingSymbol(String name1, String name2) {
        if (name1.equals(name2)) return true;
        if (Math.abs(name1.length() - name2.length()) != 1) return false;
        int error = 0;
        if (name1.length() > name2.length()){
            String name3 = name1;
            name1 = name2;
            name2 = name3;
        }
        for (int i = 0; i < name1.length(); ) {
            if (name1.charAt(i) == name2.charAt(i + error)) {
                i++;
            } else {
                if (error == 0) error++;
                else
                    return false;
            }
        }
        return true;

    }

    /**
     * The method of checking that 2 words differ by one added character
     * @param name1 - first word
     * @param name2 - second word
     * @return - the difference is no more than one added character
     */
    public static boolean isRedundantSymbol(String name1, String name2) {
        return isMissingSymbol(name1, name2);
    }

    /**
     * The method of checking that 2 words differ in the meaning of one character
     * @param name1 - first word
     * @param name2 - second word
     * @return - the difference is no more than one wrong character
     */
    public static boolean isWrongSymbol(String name1, String name2) {
        if (name1.equals(name2)) return true;

        if (name1.length() != name2.length()) return false;
        int errors = 0;
        for (int i = 0; i < name1.length(); i++) {
            if (name1.charAt(i) != name2.charAt(i)) errors++;
            if (errors > 1) return false;
        }
        return true;

    }

    /**
     * The method of checking that 2 words differ by 1 characters swapped places
     * @param name1 - first word
     * @param name2 - second word
     * @return - the difference is no more than 2 characters swapped
     */
    public static boolean isMisplacedSymbol(String name1, String name2) {
        if (name1.equals(name2)) return true;

        if (name1.length() != name2.length()) return false;
        int[] indexesMisplaced = new int[2];
        int errors = 0;
        for (int i = 0; i < name1.length(); i++) {
            if (name1.charAt(i) != name2.charAt(i)) {
                if (errors > 1) return false;
                indexesMisplaced[errors] = i;
                errors++;
            }
        }

        return name1.charAt(indexesMisplaced[0]) == name2.charAt(indexesMisplaced[1])
                && name1.charAt(indexesMisplaced[1]) == name2.charAt(indexesMisplaced[0]);

    }
}
