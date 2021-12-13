package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.enumUtils.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Projects, Long> {
    public Optional<Projects> findProjectsByProjectId(String id);

    @Query("select pr from Projects pr where pr.account.id = :id")
    public Page<Projects> findProjectsByAccountId(@Param("id") long id, Pageable pageable);

    @Query("select pr from Projects pr where pr.projectStatus = :projectStatus and pr.account.id =:id")
    public Page<Projects> findProjectsByProjectIdAndProjectStatus(@Param("id") long id, @Param("projectStatus") ProjectStatus projectStatus, Pageable pageable);
}
