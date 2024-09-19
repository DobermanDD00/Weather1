package com.weather1.weather1.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ErrorsServiceTest {

    @Test
    void generateAllWithOneError() {
        String string = "0123456789";
        List<String> strs;
        Set<String> set;
        for (int i = 0; i <= 10; i++) {
            strs = ErrorsService.generateAllWithOneError(string.substring(0, i));
            int size = strs.size();
            set = new HashSet<>(strs);
            int setSize = set.size();
            int planeSize = 53 * i + 26 + (i * i - i) / 2;

            assertEquals(true, size == planeSize);
            assertEquals(true, size == setSize);

        }
    }

    @Test
    void isOneError() {
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "Tokyo"));
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "Toko"));
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "Tokjyo"));
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "ookyT"));
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "Todkyo"));
        assertTrue(ErrorsService.isOneErrorOrLess("Tokyo", "okyo"));
        assertFalse(ErrorsService.isOneErrorOrLess("Tokyo", "Too"));
        assertFalse(ErrorsService.isOneErrorOrLess("Tokyo", "sfdsf"));
        assertFalse(ErrorsService.isOneErrorOrLess("Tokyo", "T"));
        assertFalse(ErrorsService.isOneErrorOrLess("Tokyo", ""));
        assertFalse(ErrorsService.isOneErrorOrLess("Tokyo", "Tsdfvsfoo"));

    }

    @Test
    void isMissingSymbol() {
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Tokyo"));
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Tkyo"));
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Toyo"));
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Toko"));
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Toky"));
        assertTrue(ErrorsService.isMissingSymbol("Tokyo", "Toktyo"));
        assertFalse(ErrorsService.isMissingSymbol("Tokyo", "Too"));
        assertFalse(ErrorsService.isMissingSymbol("Tokyo", "Tofkydo"));
        assertFalse(ErrorsService.isMissingSymbol("Tokyo", "Tokystho"));
        assertFalse(ErrorsService.isMissingSymbol("Tokyo", ""));
        assertFalse(ErrorsService.isMissingSymbol("Tokyo", "Tdtyjedtyhed"));
    }

    @Test
    void isRedundantSymbol() {
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "Tokyo"));
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "Todkyo"));
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "dTokyo"));
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "Tokyod"));
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "Tokydo"));
        assertTrue(ErrorsService.isRedundantSymbol("Tokyo", "Todkyo"));
        assertFalse(ErrorsService.isRedundantSymbol("Tokyo", "Toffkyo"));
        assertFalse(ErrorsService.isRedundantSymbol("Tokyo", "Tofyo"));
        assertFalse(ErrorsService.isRedundantSymbol("Tokyo", "Tyo"));
        assertFalse(ErrorsService.isRedundantSymbol("Tokyo", ""));
    }

    @Test
    void isWrongSymbol() {
        assertTrue(ErrorsService.isWrongSymbol("Tokyo", "Tokyo"));
        assertTrue(ErrorsService.isWrongSymbol("Tokyo", "dokyo"));
        assertTrue(ErrorsService.isWrongSymbol("Tokyo", "Tbkyo"));
        assertTrue(ErrorsService.isWrongSymbol("Tokyo", "Togyo"));
        assertTrue(ErrorsService.isWrongSymbol("Tokyo", "Tokyg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "Tfkyg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "Tfkyg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "Tkyfg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "Tdkyg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "Tozsdkyg"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", "TokyaESdfag"));
        assertFalse(ErrorsService.isWrongSymbol("Tokyo", ""));
    }

    @Test
    void isMisplacedSymbol() {
        assertTrue(ErrorsService.isMisplacedSymbol("Tokyo", "Tokyo"));
        assertTrue(ErrorsService.isMisplacedSymbol("Tokyo", "ookyT"));
        assertTrue(ErrorsService.isMisplacedSymbol("Tokyo", "Toyko"));
        assertTrue(ErrorsService.isMisplacedSymbol("Tokyo", "Tkoyo"));

        assertFalse(ErrorsService.isMisplacedSymbol("Tokyo", "koToy"));
        assertFalse(ErrorsService.isMisplacedSymbol("Tokyo", "Toasdkyo"));
        assertFalse(ErrorsService.isMisplacedSymbol("Tokyo", ""));

    }
}
