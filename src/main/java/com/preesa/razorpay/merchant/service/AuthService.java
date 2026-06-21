package com.preesa.razorpay.merchant.service;

import com.preesa.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.preesa.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    public MerchantResponse signUp(MerchantSignUpRequest merchantSignUpRequest);
}
