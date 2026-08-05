package com.preesa.razorpay.merchant.security;

import com.preesa.razorpay.merchant.cache.ApiKeyCache;
import com.preesa.razorpay.merchant.cache.ApiKeyCacheEntry;
import com.preesa.razorpay.merchant.entity.ApiKey;
import com.preesa.razorpay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final String BASIC_PREFIX = "Basic ";
    private final BCryptPasswordEncoder BCRYPT= new BCryptPasswordEncoder();
    private final  ApiKeyRepository apiKeyRepository;
    private final MerchantContext merchantContext;
    private final ApiKeyCache apiKeyCache;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String rawString = authorizationHeader.substring(BASIC_PREFIX.length());

            String[] decoded = decode(rawString);
//            assert decoded != null;
            String keyId = decoded[0];
            String secretKey = decoded[1];

        // Implementing Redis Cache:-

//            ApiKey apiKey = apiKeyRepository.findByKeyId(keyId)
//                    .orElseThrow(() -> new BadRequestException("Invalid or Missing API Key"));

           ApiKeyCacheEntry apiKeyCacheEntry = apiKeyCache.get(keyId).orElse(null);
//                   .orElse(loadAndCache(keyId));
           if(apiKeyCacheEntry== null){
               apiKeyCacheEntry= loadAndCache(keyId);
            }

            if (apiKeyCacheEntry != null && !apiKeyCacheEntry.enabled() || !secretKeyMatches(apiKeyCacheEntry, secretKey)) {
                throw new BadRequestException("Disabled API Key or Mismatch Secret Key ");
            }


            var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                    List.of(new SimpleGrantedAuthority("API_KEY_ROLE_")));

            SecurityContextHolder.getContext().setAuthentication(auth);
            merchantContext.setMerchantId(apiKeyCacheEntry.merchantId());
            merchantContext.setKeyId(keyId);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // Send Exceptions of Spring layer to GlobalExceptionHandler as it not reached to MVC layer.
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }


    }

    private ApiKeyCacheEntry loadAndCache(String keyId) {
        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
        if(apiKey == null){
            return null;
        }
        ApiKeyCacheEntry apiKeyCacheEntry= new ApiKeyCacheEntry(apiKey.getMerchant().getId(),
                apiKey.getKeyId(),
                apiKey.getKeySecretHash(),
                apiKey.getPreviousKeySecretHash(),
                apiKey.getEnvironment(),
                apiKey.getEnabled(),
                apiKey.getGracePeriodExpiresAt());
        apiKeyCache.put(keyId,apiKeyCacheEntry);
        return apiKeyCacheEntry;
    }

    private boolean secretKeyMatches(ApiKeyCacheEntry apiKey, String secretKey) {
        String hashedSecretKey=apiKey.keySecretHash();
        if (new BCryptPasswordEncoder().matches(secretKey,hashedSecretKey )) {
            return true;
        }
       // boolean graceTime = apiKey.getGracePeriodExpiresAt() != null && Instant.now().isBefore(apiKey.getGracePeriodExpiresAt());
        return apiKey.isInGracePeriod() && apiKey.previousKeySecretHash() != null && BCRYPT.matches(secretKey, apiKey.previousKeySecretHash());
    }

    private String[] decode(String rawString) {
        String decoded = new String(Base64.getDecoder().decode(rawString), StandardCharsets.UTF_8);
        int colon = decoded.indexOf(':');
        if (colon < 1) {
            return null;
        }
        return new String[]{decoded.substring(0, colon), decoded.substring(colon + 1)};
    }
}
