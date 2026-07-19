package com.preesa.razorpay.payment.simulator;

import com.preesa.razorpay.common.enums.ChaosMode;
import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.payment.entity.Payment;
import com.preesa.razorpay.payment.repository.PaymentRepository;
import com.preesa.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

    @Scheduled(fixedRateString = "${payment.simulator.poll-interval-ms:500000000}" )
    public void processCallBacks(){
        Instant globalWindow = Instant.now().minusSeconds(1);
        List<Payment>paymentList= paymentRepository
                .findAllByStatusAndUpdatedAtBefore(PaymentStatus.AUTHORIZING,globalWindow);

        if(paymentList == null) return;

        for(Payment payment: paymentList){
            simulateCallback(payment);
        }

    }

    private void simulateCallback(Payment payment){
        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig = simulatorConfig.configOf(payment.getMethod());

        Instant dueAt = dueAt(payment,methodSimulatorConfig);
        if(Instant.now().isBefore(dueAt)){
            return;
        }
        ChaosMode mode =simulatorConfig.getChaosMode();
        switch(mode){
            case SUCCESS -> resolve(payment,true);
            case FAILURE -> resolve(payment,false);
            case TIMEOUT -> log.debug("Payment Timeout ");
            case NORMAL -> resolve(payment, shouldApprove(payment,methodSimulatorConfig));

        }

    }

    // Used to get success/failure rate for Normal chaosMode how many request we can make success or failure
    private boolean shouldApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig) {
        int rate = Math.abs(payment.getId().hashCode()) % 100;
        return rate < methodSimulatorConfig.getSuccessRate();
    }

    private void resolve(Payment payment, boolean isApproved){
        if(isApproved){
            String bankRef= "SIM_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(),isApproved,bankRef,null,null);
        }
        else{
            paymentService.resolveAuthorization(payment.getId(),isApproved,null,"SIM_FAIL","Error Occured at Simulator");
        }
    }

    // To get due time to resolve only
    private Instant dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig) {
        int range = methodSimulatorConfig.getMaxDelaySeconds()- methodSimulatorConfig.getMinDelaySeconds();
        int delaySec = methodSimulatorConfig.getMinDelaySeconds() + (Math.abs(payment.getId().hashCode()) % (range+1));

        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW){
            delaySec = delaySec*2;
        }

        return payment.getUpdatedAt().plusSeconds(delaySec);
    }


}
