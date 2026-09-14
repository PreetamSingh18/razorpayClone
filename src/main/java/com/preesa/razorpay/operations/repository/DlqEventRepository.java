package com.preesa.razorpay.operations.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preesa.razorpay.operations.entity.DlqEvent;

public interface DlqEventRepository extends JpaRepository<DlqEvent,UUID> {
    
}
