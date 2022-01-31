package com.amsabots.jenzi.fundi_service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author andrew mititi on Date 1/31/22
 * @Project lameck-fundi-service
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RedisLocationAlgo {
    private String AccountId;
    private  double distance;
}
