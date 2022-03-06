package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.TrainedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TrainedByRepo extends JpaRepository<TrainedBy, Long> {
    Set<TrainedBy> findAllByUserId(long userId);
}
