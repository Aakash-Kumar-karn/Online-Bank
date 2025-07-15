package com.bank.insurance.insurance_service.security;

import com.bank.insurance.insurance_service.exception.TokenExpiredException;
import com.bank.insurance.insurance_service.exception.TokenMalformedException;
import com.bank.insurance.insurance_service.exception.TokenMissingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);

            try {
                email = jwtUtil.extractUsername(token);
            } catch (IllegalArgumentException | MalformedJwtException e) {
                throw new TokenMalformedException("Invalid JWT token");
            } catch (ExpiredJwtException e) {
                throw new TokenExpiredException("JWT token has expired");
            }
        } else {
            throw new TokenMissingException("JWT token is missing or malformed");
        }

        try {
            if (jwtUtil.isTokenExpired(token)) {
                SecurityContextHolder.clearContext();
            } else {
                String role = jwtUtil.extractRole(token);
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new TokenMalformedException("Token validation failed");
        }

        filterChain.doFilter(request, response);
    }
}
