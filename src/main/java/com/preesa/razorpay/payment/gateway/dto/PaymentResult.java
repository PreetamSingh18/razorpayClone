package com.preesa.razorpay.payment.gateway.dto;

public  sealed interface PaymentResult  permits PaymentResult.Pending, PaymentResult.Failure {

    record Pending (String processorRef) implements PaymentResult{

    }

    record Failure(String errorCode, String errorDescription) implements PaymentResult{

    }

}
