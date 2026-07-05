package com.preesa.razorpay.payment.strategy;

import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.payment.gateway.dto.PaymentResult;
import com.preesa.razorpay.payment.processor.PaymentProcessor;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;

import java.util.UUID;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    /**
     * @param request
     * @return
     */
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request)  {

        final String bankFail= "BANK_FAIL";

        String BankName= request.methodDetails()!=null ? request.methodDetails().get("BANK").toString() : null;

        if(bankFail.equals(BankName)){
            return new PaymentProcessorResponse.Failure("NET_BANKING_PROCESSOR_REJECTED","Bank Rejected the NetBanking Request paymentId: "+ request.paymentId());

        }

        String processorRef = "NET_BANKING_REFERENCE_"+ RandomizerUtil.randomBase64(24);

        String bankReference = "https://:redirect.com"+processorRef;

        return new PaymentProcessorResponse.Success(processorRef,bankReference);


    }

    /**
     * @param paymentId
     * @return
     */
    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("NET_BANKING_REF");
    }
}
