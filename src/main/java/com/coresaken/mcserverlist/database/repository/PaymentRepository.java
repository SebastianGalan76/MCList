package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
