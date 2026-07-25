package com.preesa.razorpay.merchant.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtill jwtUtill;
    private final MerchantContext merchantContext;
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

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = authorizationHeader.substring("Bearer ".length());

            Claims claims = jwtUtill.verifyAccessToken(jwtToken);

            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
                        List.of(new SimpleGrantedAuthority("ROLE_" + jwtUtill.extractRole(claims))));

                SecurityContextHolder.getContext().setAuthentication(auth);
                merchantContext.setMerchantId(UUID.fromString(jwtUtill.extractMerchantId(claims)));
            }

            filterChain.doFilter(request, response);
        }
        catch ( Exception ex){
            // Send Exceptions of Spring layer to GlobalExceptionHandler as it not reached to MVC layer.
             handlerExceptionResolver.resolveException(request,response,null,ex);
        }

    }
}
