package com.preesa.razorpay.merchant.serviceImpl;

import com.preesa.razorpay.common.enums.MerchantStatus;
import com.preesa.razorpay.common.enums.UserRole;
import com.preesa.razorpay.common.exceptions.DuplicateResourceException;
import com.preesa.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.preesa.razorpay.merchant.dto.response.MerchantResponse;
import com.preesa.razorpay.merchant.entity.AppUser;
import com.preesa.razorpay.merchant.entity.Merchant;
import com.preesa.razorpay.merchant.repository.AppUserRepository;
import com.preesa.razorpay.merchant.repository.MerchantRepository;
import com.preesa.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;

    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public MerchantResponse signUp(MerchantSignUpRequest merchantSignUpRequest) {
        if(merchantRepository.existsByEmail(merchantSignUpRequest.email())){
                  throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant already exist with emailId :" +merchantSignUpRequest.email());
        }

        Merchant merchant =Merchant.builder()
                .email(merchantSignUpRequest.email())
                .businessName(merchantSignUpRequest.businessName())
                .businessType(merchantSignUpRequest.businessType())
                .name(merchantSignUpRequest.name())
                .build();

        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(merchantSignUpRequest.email())
                .merchant(merchant)
                .passwordHash(merchantSignUpRequest.password()) //TODO: Encrypt the password
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);

        return new MerchantResponse(merchant.getId(),merchant.getName(),merchant.getEmail(),merchant.getBusinessName(),merchant.getBusinessType(),merchant.getStatus());

    }
}
