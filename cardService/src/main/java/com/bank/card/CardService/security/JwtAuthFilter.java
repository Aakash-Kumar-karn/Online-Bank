package com.bank.card.CardService.security;

import com.bank.card.CardService.exception.TokenExpiredException;
import com.bank.card.CardService.exception.TokenMalformedException;
import com.bank.card.CardService.exception.TokenMissingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//
//    private final JwtUtil jwtUtil;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // 🧾 Extract Authorization header
//        String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//        String email = null;
//        String token = null;
//
//        if (requestToken != null && requestToken.startsWith("Bearer ")) {
//            token = requestToken.substring(7);
//
//            try {
//                email = this.jwtUtil.extractUsername(token); // typically the subject
//            } catch (IllegalArgumentException e) {
//                throw new TokenMalformedException(" Unable to parse JWT token");
//            } catch (ExpiredJwtException e) {
//                throw new TokenExpiredException(" JWT token has expired");
//            } catch (MalformedJwtException e) {
//                throw new TokenMalformedException(" JWT token is malformed");
//            }
//        } else {
//            throw new TokenMissingException("JWT token is missing or does not begin with 'Bearer '");
//        }
//
//        try {
//            if (jwtUtil.isTokenExpired(token)) {
//                SecurityContextHolder.clearContext(); // expired token
//            } else {
//                String username = jwtUtil.extractUsername(token);
//                String role = jwtUtil.extractRole(token);
//
////  Create authorities like ROLE_USER or ROLE_ADMIN
//                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
//                //  Create Spring Security authentication token
//                UsernamePasswordAuthenticationToken auth =
//                        new UsernamePasswordAuthenticationToken(username, null, authorities);
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        } catch (Exception e) {
//            // Invalid token: just skip setting authentication
//            SecurityContextHolder.clearContext(); // fallback on invalid token
//            throw new TokenMalformedException(" Token validation failed");
//        }
//        //  Continue the filter chain
//             filterChain.doFilter(request, response);
//    }
//}
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = null;
        String token = null;

        System.out.println(" Incoming request uri: " + request.getRequestURI());
        System.out.println(" Authorization Header: " + requestToken);

        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);
            System.out.println("  Extracted JWT Token: " + token);

            try {
                email = this.jwtUtil.extractUsername(token); // typically the subject
                System.out.println(" Username from Token: " + email);
            } catch (IllegalArgumentException e) {
                throw new TokenMalformedException("Unable to parse JWT token");
            } catch (ExpiredJwtException e) {
                throw new TokenExpiredException("JWT token has expired");
            } catch (MalformedJwtException e) {
                throw new TokenMalformedException("JWT token is malformed");
            }
        } else {
            throw new TokenMissingException("JWT token is missing or does not begin with 'Bearer '");
        }

        try {
            if (jwtUtil.isTokenExpired(token)) {
                System.out.println("Token is expired, clearing security context");
                SecurityContextHolder.clearContext();
            } else {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                System.out.println(" Valid token. Username: " + username + ", Role: " + role);

                // Don't prepend ROLE_ if it's already present in token
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
              }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new TokenMalformedException("Token validation failed");
        }

        filterChain.doFilter(request, response);
    }
}
