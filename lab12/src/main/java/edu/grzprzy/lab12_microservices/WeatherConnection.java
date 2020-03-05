package edu.grzprzy.lab12_microservices;

import edu.grzprzy.lab12_microservices.tables.Location;
import edu.grzprzy.lab12_microservices.tables.Measurement;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class WeatherConnection {
    private static final String API_KEY = "3a36a49f758bbf36f9e04cf13c14e471";
    private static final String WEATHER_REST_URL = "http://api.openweathermap.org/data/2.5/weather?appid=" + API_KEY + "&units=metric";

    public static Map<String, Object> getWeatherByCityName(String city_name) {
        RestTemplate templateClient = new RestTemplate();
        String finalUrl = WEATHER_REST_URL + "&q=" + city_name;
        Map<String, Object> responseBody = templateClient.getForObject(finalUrl,Map.class);
        return responseBody;
    }

    public static Map<String, Object> getWeatherByLocation(float lat, float lon) {
        RestTemplate templateClient = new RestTemplate();
        String finalUrl = WEATHER_REST_URL + "&lat=" + lat + "&lon=" + lon;
        Map<String, Object> responseBody = templateClient.getForObject(finalUrl,Map.class);
        return responseBody;
    }

    public static Location getLocationByCityName(String city_name) {
        Map<String, Object> responseBody = getWeatherByCityName(city_name);
        return getLocationByResponseBody(responseBody);
    }

    public static Location getLocationByCoordinates(Float lat, Float lon) {
        Map<String, Object> responseBody = getWeatherByLocation(lat,lon);
        return getLocationByResponseBody(responseBody);
    }

    public static Measurement getMeasurementByCoordinates(Float lat, Float lon) {
        Map<String, Object> responseBody = getWeatherByLocation(lat,lon);
        return getMeasurementByResponseBody(responseBody);
    }

    public static Measurement getMeasurementByCityName(String cityName) {
        Map<String, Object> responseBody = getWeatherByCityName(cityName);
        return getMeasurementByResponseBody(responseBody);
    }

    private static Location getLocationByResponseBody(Map<String, Object> responseBody) {
        Location location = new Location();
        Map<String, Double> coordinates = (Map<String, Double>)responseBody.get("coord");
        location.setLat(coordinates.get("lat").floatValue());
        location.setLon(coordinates.get("lon").floatValue());
        location.setCity_name((String)responseBody.get("name"));
        return location;
    }

    private static Measurement getMeasurementByResponseBody(Map<String, Object> responseBody) {
        Measurement toRet = new Measurement();
        Map<String, Double> airCondition = (Map<String, Double>)responseBody.get("main");
        toRet.setTemperature(airCondition.get("temp").floatValue());
        return toRet;
    }
}
