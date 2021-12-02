package com.amsabots.jenzi.fundi_service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
@AllArgsConstructor
@Data
public class ResponseObject<T> {
    private List<T> data;
    private int pageSize;
    private int currentPage;
}
