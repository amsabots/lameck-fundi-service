package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.RatesAndReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RateAndReviewRepo extends JpaRepository<RatesAndReviews, Long> {
    @Query("select rt from RatesAndReviews rt where rt.account.id = :id")
    public Page<RatesAndReviews> findAllByAccountId(@Param("id") long id, Pageable pageable);

    public Optional<RatesAndReviews> getRatesAndReviewsBySource(String source);

    public Optional<RatesAndReviews> getRatesAndReviewsByReviewId(String reviewId);
}
