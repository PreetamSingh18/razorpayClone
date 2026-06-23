package com.preesa.razorpay.merchant.mapper;

import com.preesa.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.preesa.razorpay.merchant.dto.response.MerchantResponse;
import com.preesa.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest (MerchantSignUpRequest merchantSignUpRequest);

    @Mapping(target = "merchantStatus", source = "status")
    MerchantResponse toResponse (Merchant merchant);
}
