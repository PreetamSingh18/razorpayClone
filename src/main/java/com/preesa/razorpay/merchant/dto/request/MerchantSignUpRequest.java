package com.preesa.razorpay.merchant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.preesa.razorpay.common.enums.BusinessType;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record MerchantSignUpRequest(

        @NotNull
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "password is required")
        @Size(min = 8, message = "password must me greater than 7 character")
        String password,

        @NotNull
        @Size(max = 50, message = "business name should not be greater than 50 character")
        String businessName,
        BusinessType businessType
) {




}
