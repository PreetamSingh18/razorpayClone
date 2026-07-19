package com.preesa.razorpay.payment.simulator;

import com.preesa.razorpay.common.enums.ChaosMode;
import com.preesa.razorpay.common.enums.PaymentMethod;
import com.preesa.razorpay.payment.entity.Payment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "payment.simulator")
@Getter
@Setter
public class SimulatorConfig {
    private Integer pollIntervalMs = 2000;
    private ChaosMode chaosMode = ChaosMode.NORMAL;
    private Map<String, MethodSimulatorConfig> methods = new HashMap<>();


    MethodSimulatorConfig configOf(PaymentMethod paymentMode){
        return methods.getOrDefault(paymentMode.name(),new MethodSimulatorConfig());
    }

    @Getter
    @Setter
    public static class MethodSimulatorConfig{
        private Integer minDelaySeconds= 3;
        private Integer maxDelaySeconds = 10;
        private Integer successRate= 95;
    }
}
