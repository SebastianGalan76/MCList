package com.coresaken.mcserverlist.database.model;

import com.coresaken.mcserverlist.data.payment.PaymentAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 64)
    String serviceId;

    String paymentId;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(length = 1000)
    String successAction;

    @Enumerated(EnumType.STRING)
    PaymentAction action;

    public enum Status {
        DEFAULT, SUCCESS, PENDING, FAILURE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment city = (Payment) o;
        return Objects.equals(id, city.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}