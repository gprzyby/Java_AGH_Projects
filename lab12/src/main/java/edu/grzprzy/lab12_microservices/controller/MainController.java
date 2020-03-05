package edu.grzprzy.lab12_microservices.controller;

import edu.grzprzy.lab12_microservices.WeatherConnection;
import edu.grzprzy.lab12_microservices.jobs.WeatherCheckingJob;
import edu.grzprzy.lab12_microservices.repositories.LocationRepo;
import edu.grzprzy.lab12_microservices.repositories.MeasurementRepo;
import edu.grzprzy.lab12_microservices.tables.Location;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;



@RestController
@RequestMapping("/api")
public class MainController {

    private final LocationRepo locationRepo;

    private final MeasurementRepo measurementRepo;

    private final SchedulerFactoryBean factory;

    @Autowired
    public MainController(LocationRepo locationRepo, MeasurementRepo measurementRepo, SchedulerFactoryBean factory) {
        this.locationRepo = locationRepo;
        this.measurementRepo = measurementRepo;
        this.factory = factory;
    }

    @GetMapping("/weather/city/{cityName}")
    public ResponseEntity<Map<String, Object>> getWeatherForCity(@PathVariable String cityName) {
        try{
            Map<String,Object> toRet = WeatherConnection.getWeatherByCityName(cityName);
            return new ResponseEntity<>(toRet, HttpStatus.OK);
        } catch(HttpClientErrorException.NotFound exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        } catch(RestClientException exc) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong with rest");
        }
    }

    @GetMapping("/weather/city/")
    public ResponseEntity<Map<String, Object>> getWeatherForCity(@RequestParam Float lat, @RequestParam Float lon) {
        try{
            Map<String,Object> toRet = WeatherConnection.getWeatherByLocation(lat,lon);
            return new ResponseEntity<>(toRet, HttpStatus.OK);
        } catch(HttpClientErrorException.NotFound exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        } catch(RestClientException exc) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong with rest");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Location> startCheckingWeatherForLocation(@RequestBody Map<String,Object> requestBody) {
        //checking data
        if(requestBody.size() == 0 && !requestBody.containsKey("freq")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty or without freq request body");
        try {
            int freq = (Integer) requestBody.get("freq");
            if (freq <= 0 ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time cannot be <=0");
            }
            if (requestBody.containsKey("lat") && requestBody.containsKey("lon")) {
                //search by location
                float lat = ((Double) requestBody.get("lat")).floatValue();
                float lon = ((Double) requestBody.get("lon")).floatValue();
                Location toRet = locationRepo.save(getLocationByLocation(lat, lon, freq));
                startScheduler(toRet, freq,false);
                return new ResponseEntity<>(toRet,HttpStatus.OK);
            } else if (requestBody.containsKey("city")) {
                //searchByCity
                Location toRet = locationRepo.save(getLocationByCity((String) requestBody.get("city"), freq));
                startScheduler(toRet, freq,false);
                return new ResponseEntity<>(toRet,HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body not supported. Use city or lat, lon in request body");
            }

        } catch (SchedulerException exc) {
            exc.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
        } catch (HttpClientErrorException.NotFound exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        } catch (ClassCastException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request argument types");
        } catch (HttpClientErrorException.BadRequest exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage());
        }
    }

    @GetMapping("/stat/{ID}/{n}")
    public ResponseEntity<Map<String, Object>> getStatForLocation(@PathVariable String ID, @PathVariable String n) {
        //checking input data
        int location_id, measures;
        try{
            location_id = Integer.valueOf(ID);
            measures = Integer.valueOf(n);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patch variables wasn't an numbers");
        }
        //checking if location exists on specified id
        if(!locationRepo.existsById(location_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No location at specified id");
        }

        if(measures <=0 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount of measures must be grater than 0");
        }

        Map<String, Object> queryAnswer = measurementRepo.getAvgTempFromMeasures(location_id,measures);

        return new ResponseEntity<>(queryAnswer, HttpStatus.OK);
    }


    private Location getLocationByCity(String cityName, long periodInSec) throws SchedulerException {
        Location location = WeatherConnection.getLocationByCityName(cityName);
        location.setUpdate_freq((int) periodInSec);
        return location;

    }

    private Location getLocationByLocation(float lat, float lon, long periodInSec) throws SchedulerException {
        Location location = WeatherConnection.getLocationByCoordinates(lat, lon);
        location.setUpdate_freq((int) periodInSec);
        return location;
    }

    private void startScheduler(Location location, long periodInSec, boolean searchByCity) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        JobDataMap arguments = new JobDataMap();
        arguments.put("location", location);
        arguments.put("searchByCity", searchByCity);
        arguments.put("measurementRepo", measurementRepo);

        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(WeatherCheckingJob.class);
        jobDetailFactoryBean.setJobDataMap(arguments);
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.afterPropertiesSet();

        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(jobDetailFactoryBean.getObject());
        trigger.setRepeatInterval(periodInSec * 1000L);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.afterPropertiesSet();

        scheduler.scheduleJob(jobDetailFactoryBean.getObject(),trigger.getObject());
        factory.start();
    }


}
