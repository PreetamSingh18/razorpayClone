package com.preesa.razorpay.payment.repository;

import com.preesa.razorpay.common.enums.OutBoxStatus;
import com.preesa.razorpay.payment.entity.OutBoxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutBoxEvent, UUID> {

    List<OutBoxEvent> findByStatusOrderByCreatedAtAsc(OutBoxStatus status);
}
