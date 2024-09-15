package com.weather1.weather1.models.Weather;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ToString
@Component
public class Weather {
    Location location;
    Current current;
}
