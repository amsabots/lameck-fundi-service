package com.amsabots.jenzi.fundi_service.utils;


import com.amsabots.jenzi.fundi_service.entities.Fundi_Account_Overall_Perfomance;
import com.amsabots.jenzi.fundi_service.enumUtils.AccountPerformanceEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/*
 * An object representing the type of action to perform and the Performance Object to update.
 *
 * */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerfomanceObjectUtil {
    @Enumerated(EnumType.STRING)
    private AccountPerformanceEnum anEnum;
    private Fundi_Account_Overall_Perfomance overallPerfomance;
}
