package com.preesa.razorpay.payment.gateway.dto;

public  sealed interface PaymentResult permits PaymentResult.Failure, PaymentResult.Pending, PaymentResult.Success {

    record Pending (String processorRef) implements PaymentResult{

    }

    record Failure(String errorCode, String errorDescription) implements PaymentResult{

    }

    record Success(String bankReference) implements PaymentResult{

    }

}
