package com.weather1.weather1.controllers;


import com.weather1.weather1.models.City;
import com.weather1.weather1.models.Weather.Weather;
import com.weather1.weather1.service.CityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CityService service;

    @Test
    void showWeather() throws Exception {
        mockMvc.perform(get("/weather"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void showWeathersInCites() throws Exception {
        String cityName = "Tokyo";

        int[] optionsSearch = new int[]{WeatherController.SEARCH_IN_FILE, WeatherController.SEARCH_IN_DB_WITH_GENERATION_CITES_SET, WeatherController.SEARCH_IN_DB_WITH_HASH};
        when(service.findCityWithOneErrorInFile(cityName)).thenReturn(Arrays.asList(new City(1L, cityName)));
        when(service.findCityWithOneErrorInDbWithGenerationCitesSet(cityName)).thenReturn(Arrays.asList(new City(1L, cityName)));
        when(service.findCityWithOneErrorInDbWithHash(cityName)).thenReturn(Arrays.asList(new City(1L, cityName)));

        for (int optionSearch : optionsSearch) {

            Map<String, Object> model = mockMvc.perform(post("/weather").param("city", cityName).param("optionSearch", Integer.toString(optionSearch)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("reportCity"))
                    .andExpect(view().name("weathers"))
                    .andReturn().getModelAndView().getModel();
            List<Weather> weathers = (List<Weather>) model.get("weathers");
            assertTrue(weathers != null && weathers.size() > 0);
            assertTrue(weathers.get(0).getCurrent() != null);
        }


    }


}