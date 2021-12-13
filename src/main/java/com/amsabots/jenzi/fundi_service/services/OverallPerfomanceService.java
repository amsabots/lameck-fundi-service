package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.entities.Fundi_Account_Overall_Perfomance;
import com.amsabots.jenzi.fundi_service.enumUtils.AccountPerformanceEnum;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomResourceNotFound;
import com.amsabots.jenzi.fundi_service.repos.Account_Overall_Repo;
import com.amsabots.jenzi.fundi_service.utils.PerfomanceObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OverallPerfomanceService {
    @Autowired
    private Account_Overall_Repo repo;

    public Fundi_Account_Overall_Perfomance createOrUpdate(Fundi_Account_Overall_Perfomance overallPerfomance) {
        return repo.save(overallPerfomance);
    }

    public Fundi_Account_Overall_Perfomance getOveralPerfomance(long id) {
        return repo.findFundi_Account_Overall_PerfomanceByAccount_Id(id).orElseThrow(() ->
                new CustomResourceNotFound("Overall account performance for this particular account does not exist fro some unknown reasons"));
    }

    public void deleteAccountPerfomance(long id) {
        repo.deleteFundi_Account_Overall_PerfomanceByAccount_Id(id);
    }

    public void account_overall_performance(PerfomanceObjectUtil objectUtil) {
        Fundi_Account_Overall_Perfomance overall_perfomance = repo.findById(objectUtil.getOverallPerfomance().getId()).get();
        switch (objectUtil.getAnEnum()) {
            case CLIENTS_RATED:
                if (shouldIncrease(objectUtil.getOverallPerfomance().getClient_rated()))
                    overall_perfomance.setClient_rated(overall_perfomance.getClient_rated() + 1);
                else
                    overall_perfomance.setClient_rated(overall_perfomance.getClient_rated() - 1);
                break;
            case COMPLETED_PROJECTS:
                if (shouldIncrease(objectUtil.getOverallPerfomance().getCompletedProjects()))
                    overall_perfomance.setCompletedProjects(overall_perfomance.getCompletedProjects() + 1);
                else
                    overall_perfomance.setCompletedProjects(overall_perfomance.getCompletedProjects() - 1);
                break;
            case DISPUTES_RAISED:
                if (shouldIncrease(objectUtil.getOverallPerfomance().getDisputesRaised()))
                    overall_perfomance.setDisputesRaised(overall_perfomance.getDisputesRaised() + 1);
                else
                    overall_perfomance.setDisputesRaised(overall_perfomance.getDisputesRaised() - 1);
                break;
            case ONGOING_PROJECTS:
                if (shouldIncrease(objectUtil.getOverallPerfomance().getOngoingProjects()))
                    overall_perfomance.setOngoingProjects(overall_perfomance.getOngoingProjects() + 1);
                else
                    overall_perfomance.setOngoingProjects(overall_perfomance.getOngoingProjects() - 1);
                break;
            case REVIEWS_GIVEN:
                if (shouldIncrease(objectUtil.getOverallPerfomance().getReviewsGiven()))
                    overall_perfomance.setReviewsGiven(overall_perfomance.getReviewsGiven() + 1);
                else
                    overall_perfomance.setReviewsGiven(overall_perfomance.getReviewsGiven() - 1);
                break;
            case RESPONSE_TIME:
                log.info("Calculating response time");
                break;
            case STARS:
                break;
        }
        //re-save the changed object above;
        repo.save(overall_perfomance);
    }

    private boolean shouldIncrease(long arg) {
        if (arg < 0) return false;
        return true;
    }

}
