package com.amsabots.jenzi.fundi_service.services;


import com.amsabots.jenzi.fundi_service.entities.RatesAndReviews;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomResourceNotFound;
import com.amsabots.jenzi.fundi_service.repos.RateAndReviewRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RatesAndReviewService {

    private RateAndReviewRepo repo;

    public List<RatesAndReviews> getAccountRates(Long id, Pageable pageable) {
        return repo.findAllByAccountId(id, pageable).getContent();
    }

    public RatesAndReviews getReviewById(long id) {
        return repo.findById(id).orElseThrow(() -> new CustomResourceNotFound("The rate you are looking for does not exists"));
    }

    public RatesAndReviews createOrUpdateRate(RatesAndReviews ratesAndReviews) {
        return repo.save(ratesAndReviews);
    }

    public void deleteReview(long id) {
        repo.deleteById(id);
    }

    public RatesAndReviews getReviewBySource(String source) {
        return repo.getRatesAndReviewsBySource(source).orElseThrow(() ->
                new CustomResourceNotFound("The review belonging to the given account Id does not exist"));
    }
}
