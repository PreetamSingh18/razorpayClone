package com.preesa.razorpay.payment.strategy;

import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.PaymentProcessor;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentProcessor implements PaymentProcessor {

    private  static final String PAN_CARD_DECLINED = "4000000000000001";
    private  static  final String PAN_CARD_EXPIRED ="4000000000000001";

    /**
     * @param request
     * @return
     */
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        String pan= request.pan();
        if (PAN_CARD_DECLINED.equals(pan)){
            log.warn("Pan card declined");
          return new  PaymentProcessorResponse.Failure("PAN_CARD_DECLINED","Pan card is declined");
        }
        else if (PAN_CARD_EXPIRED.equals(pan)){
            log.warn("Pan card is expired");
            return new  PaymentProcessorResponse.Failure("PAN_CARD_EXPIRED","Pan card is expired");
        }

        String processorRef = "CARD_PROCESSOR_"+ RandomizerUtil.randomBase64(24);

     return new PaymentProcessorResponse.Pending(processorRef);
    }


}
