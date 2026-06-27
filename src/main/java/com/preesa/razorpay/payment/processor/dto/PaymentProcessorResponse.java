package com.preesa.razorpay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits PaymentProcessorResponse.Pending, PaymentProcessorResponse.Success, PaymentProcessorResponse.Failure {

    public record Pending(String processorReference)implements PaymentProcessorResponse{

    }

    public record Success(String processorReference , String transactionReference)implements PaymentProcessorResponse{

    }
    public record Failure(String errorCode, String errorDescription)implements PaymentProcessorResponse{

    }

}
