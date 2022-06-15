package ru.job4j.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.job4j.model.Weather;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 15));
        weathers.put(4, new Weather(4, "Smolensk", 32));
        weathers.put(5, new Weather(5, "Kiev", 30));
        weathers.put(6, new Weather(6, "Minsk", 32));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    /**
     * @return список городов с максимальной температурой
     */

    public Flux<Weather> hottest() {
        int maxTemperature = weathers.values()
                .stream()
                .mapToInt(Weather::getTemperature)
                .max()
                .orElse(-1);
        return Flux.fromIterable(weathers.values().
                stream()
                .filter(t -> t.getTemperature() == maxTemperature)
                .collect(Collectors.toList()));
    }

    /**
     * @return список городов с температурой,
     * выше входного параметра
     */

    public Flux<Weather> findGreaterThanValue(int temperature) {
        return Flux.fromIterable(
                weathers.values()
                        .stream()
                        .filter(t -> t.getTemperature() > temperature)
                        .collect(Collectors.toList()));
    }
}