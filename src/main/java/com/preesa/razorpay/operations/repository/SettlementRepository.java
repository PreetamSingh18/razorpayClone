package com.preesa.razorpay.operations.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preesa.razorpay.common.enums.SettlementStatus;
import com.preesa.razorpay.operations.entity.Settlement;


public interface SettlementRepository extends  JpaRepository<Settlement,UUID>{

   List<Settlement> findByStatus(SettlementStatus transferPending);
    
    
}
