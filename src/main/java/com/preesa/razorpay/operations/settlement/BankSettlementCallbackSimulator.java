package com.preesa.razorpay.operations.settlement;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.preesa.razorpay.common.enums.SettlementStatus;
import com.preesa.razorpay.operations.entity.Settlement;
import com.preesa.razorpay.operations.repository.SettlementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component
@RequiredArgsConstructor 
public class BankSettlementCallbackSimulator {

    private final SettlementRepository settlementRepository;
    private final SettlementTransactionExecutor settlementTransactionExecutor;

    @Scheduled (fixedDelayString = "5000")
    public void  processCallBacks(){

        List<Settlement> settlementsPending = settlementRepository.findByStatus(SettlementStatus.TRANSFER_PENDING);

        if(settlementsPending  == null){
        log.info("No Pending Settlements records found to Process");
        return;
        }

        for(Settlement settlement : settlementsPending){
            simulateCallback(settlement);
        }


    }

    private void simulateCallback(Settlement settlement) {
       log.info("Initiating Settlement callback for settlement id:{} ", settlement.getId());
       settlementTransactionExecutor.resolveTransfer(settlement.getId(),null, null);
    }
    
}
