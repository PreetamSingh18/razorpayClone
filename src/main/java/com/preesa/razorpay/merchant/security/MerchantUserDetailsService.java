package com.preesa.razorpay.merchant.security;

import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.merchant.entity.AppUser;
import com.preesa.razorpay.merchant.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MerchantUserDetailsService implements UserDetailsService {


    private final AppUserRepository appUserRepository;
    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return appUserRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("APPUSER", username));
//        return appUser;
    }
}
