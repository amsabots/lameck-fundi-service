package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.entities.RedisLocationAlgo;
import com.amsabots.jenzi.fundi_service.repos.AccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author andrew mititi on Date 1/31/22
 * @Project lameck-fundi-service
 */
@Component
@Slf4j
public class LocationGenerator {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${app.params.variables.location_key}")
    private String app_location_key;
    @Value("${app.params.variables.locator_delay}")
    private String locator_delay;

    @Autowired
    private AccountRepo repo;


    public void createAGeoHashRecord(List<Account> accounts) {
        Map<String, Point> points = new HashMap<>();
        accounts.forEach(user -> {
            log.info("Adding accountId: {} with longitude: {} and latitude: {}",
                    user.getAccountId(), user.getLongitude(), user.getLatitude());
            points.put(user.getAccountId(),
                    new Point(Double.parseDouble(user.getLongitude()), Double.parseDouble(user.getLatitude())));
        });
        BoundGeoOperations<String, String> geoOperations = redisTemplate.boundGeoOps((app_location_key));
        geoOperations.add(points);
    }

    public List<RedisLocationAlgo> queryByProximityRadius(Point point, double scanRadius) {
        List<RedisLocationAlgo> locationAlgo = new ArrayList<>();
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Distance distance = new Distance(scanRadius, metric);
        Circle circle = new Circle(point, distance);

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending();

        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo()
                .radius(app_location_key, circle, args);
        if (radius != null) {
            radius.forEach(location -> {
                RedisGeoCommands.GeoLocation<String> content = location.getContent();
                RedisLocationAlgo a = new RedisLocationAlgo();
                a.setAccountId(content.getName());
                a.setDistance(location.getDistance().getValue());
                locationAlgo.add(a);
            });
        }
        return locationAlgo;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000 * 20)
    public void populateRedisDatabaseAtIntervals() {
        log.info("Updating redis location content with new values..............");
        List<Account> accounts = repo.findAll()
                .stream()
                .filter(e -> e.getLongitude() != null)
                .collect(Collectors.toList());
        if (accounts.size() > 0) createAGeoHashRecord(accounts);
    }
}
