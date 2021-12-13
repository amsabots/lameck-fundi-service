package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.Fundi_Account_Overall_Perfomance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Account_Overall_Repo extends JpaRepository<Fundi_Account_Overall_Perfomance, Long> {

    Optional<Fundi_Account_Overall_Perfomance> findFundi_Account_Overall_PerfomanceByAccount_Id(long id);

    void deleteFundi_Account_Overall_PerfomanceByAccount_Id(long id);
}
