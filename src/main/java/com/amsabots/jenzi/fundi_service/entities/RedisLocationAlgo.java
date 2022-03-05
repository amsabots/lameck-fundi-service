package com.amsabots.jenzi.fundi_service.entities;

import lombok.*;

/**
 * @author andrew mititi on Date 1/31/22
 * @Project lameck-fundi-service
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RedisLocationAlgo {
    private String AccountId;
    private  double distance;
}
