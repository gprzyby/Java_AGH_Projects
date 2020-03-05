package edu.grzprzy.lab12_microservices.jobs;

import edu.grzprzy.lab12_microservices.WeatherConnection;
import edu.grzprzy.lab12_microservices.repositories.MeasurementRepo;
import edu.grzprzy.lab12_microservices.tables.Location;
import edu.grzprzy.lab12_microservices.tables.Measurement;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.time.LocalDate;

public class WeatherCheckingJob implements Job {

    private Location location;
    private MeasurementRepo measurementRepo;
    private boolean searchByCity = false;

    public WeatherCheckingJob() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isSearchByCity() {
        return searchByCity;
    }

    public void setSearchByCity(boolean searchByCity) {
        this.searchByCity = searchByCity;
    }

    public MeasurementRepo getMeasurementRepo() {
        return measurementRepo;
    }

    public void setMeasurementRepo(MeasurementRepo measurementRepo) {
        this.measurementRepo = measurementRepo;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Measurement fromService;
        if(searchByCity) {
            fromService = WeatherConnection.getMeasurementByCityName(location.getCity_name());
        } else {
            fromService = WeatherConnection.getMeasurementByCoordinates(location.getLat(),location.getLon());
        }
        fromService.setMeasure_date(new Date());
        fromService.setLocation(location);
        measurementRepo.save(fromService);
        System.out.println(fromService);

    }
}
