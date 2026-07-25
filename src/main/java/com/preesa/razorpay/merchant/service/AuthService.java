package com.preesa.razorpay.merchant.service;

import com.preesa.razorpay.merchant.dto.request.LoginRequest;
import com.preesa.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.preesa.razorpay.merchant.dto.response.LoginResponse;
import com.preesa.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    public MerchantResponse signUp(MerchantSignUpRequest merchantSignUpRequest);

    LoginResponse login(@Valid LoginRequest loginRequest);
}
