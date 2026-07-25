package com.preesa.razorpay.merchant.controller;


import com.preesa.razorpay.merchant.dto.request.LoginRequest;
import com.preesa.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.preesa.razorpay.merchant.dto.response.LoginResponse;
import com.preesa.razorpay.merchant.dto.response.MerchantResponse;
import com.preesa.razorpay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("v1/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse>signUp(@RequestBody @Valid MerchantSignUpRequest merchantSignUpRequest){
             return ResponseEntity.status(HttpStatus.CREATED).body(
                     authService.signUp(merchantSignUpRequest)
             );
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(
                authService.login(loginRequest)
        );
    }

}
