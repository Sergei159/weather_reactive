package ru.job4j.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.model.Weather;
import ru.job4j.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;

    public WeatherController(WeatherService weathers) {
        this.weatherService = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weatherService.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weatherService.findById(id);
    }

    @GetMapping(value = "/hottest")
    public Flux<Weather> hottest() {
        Flux<Weather> data =  weatherService.hottest();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    /**
     *
     * параметр produces отвечает за реактивность запроса
     */

    @GetMapping(value = "/cityGreaterThan/{temp}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> greaterThan(@PathVariable int temp) {
        Flux<Weather> data =  weatherService.findGreaterThanValue(temp);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }



}