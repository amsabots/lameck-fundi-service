package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepo extends JpaRepository<Payments, Long> {

    public Page<Payments> getPaymentsBySource(String source);
    public Page<Payments> getPaymentsByProjectId(long id);
}
