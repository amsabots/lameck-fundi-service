package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.entities.RatesAndReviews;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomBadRequest;
import com.amsabots.jenzi.fundi_service.services.RatesAndReviewService;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rates-review")
public class ReviewsController {

    /*
     * This endpoint should be used to test if its the root url can be reached in the first place before running
     * data handlers APIS
     * */

    @Autowired
    private RatesAndReviewService service;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<RatesAndReviews>> getAllProjects(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize
            , @RequestParam Optional<Long> accountId) {

        int current_page = page.orElse(0);
        int page_size = pageSize.orElse(20);
        long id = accountId.orElseThrow(() ->
                new CustomBadRequest("Request parameter of type accountId is required to map th request to respective account id")
        );
        List<RatesAndReviews> rates = service.getAccountRates(id, PageRequest.of(current_page, page_size));
        return ResponseEntity.ok().body(new ResponseObject<RatesAndReviews>(rates, page_size, current_page));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatesAndReviews> addReview(@RequestBody RatesAndReviews rates) {
        return ResponseEntity.ok(service.createOrUpdateRate(rates));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProject(@PathVariable long id, @RequestBody RatesAndReviews ratesAndReviews) {
        service.getReviewById(id);
        ratesAndReviews.setId(id);
        service.createOrUpdateRate(ratesAndReviews);
        return ResponseEntity.ok().body("{\"message\":\"The review and rating details have been updated successfully\"}");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeProject(@PathVariable long id) {
        service.deleteReview(id);
        return ResponseEntity.ok().body("{\"message\":\"The Review record has been removed successfully\"}");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/get")
    public ResponseEntity<RatesAndReviews> getReviewByReviewId(@RequestParam Optional<String> id,
                                                               @RequestParam Optional<String> getBy) {
        String get_by = getBy.orElseThrow(() -> new CustomBadRequest(
                "You have to explicitly specify the method to use to fetch using key \"getBy\"\n" +
                        "============ Allowed Options ==========\n" +
                        "1. source\n" +
                        "2. reviewId"
        ));
        String review_id = id.orElseThrow(() ->
                new CustomBadRequest("An existing review \"id\" Should in the request parameters - Must be a string" +
                        "with more than 10 characters"));

        if (get_by.equalsIgnoreCase("reviewId"))
            return ResponseEntity.ok().body(service.getReviewByReviewId(review_id));
        else if (get_by.equals("source"))
            return ResponseEntity.ok().body(service.getReviewBySource(review_id));
        else
            throw new CustomBadRequest("Options must include either of the following:\n" +
                    "1. source\n" +
                    "2. reviewId");
    }

}
