package com.amsabots.jenzi.fundi_service.utils;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.entities.CategoryTags;
import com.amsabots.jenzi.fundi_service.entities.RedisLocationAlgo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author andrew mititi on Date 1/31/22
 * @Project lameck-fundi-service
 */
@Getter
@Setter
@NoArgsConstructor
public class NearbyAccounts {
    private Account account;
    private double distance;
    private List<CategoryTags> tags;
}
