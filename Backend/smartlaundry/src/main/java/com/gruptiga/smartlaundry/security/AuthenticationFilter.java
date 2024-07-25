package com.gruptiga.smartlaundry.security;

import com.gruptiga.smartlaundry.dto.response.JwtClaims;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.service.JwtService;
import com.gruptiga.smartlaundry.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    final String AUTH_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AUTH_HEADER);

            // Periksa apakah token diawali dengan kata "Bearer"
            if(bearerToken != null && bearerToken.startsWith("Bearer ")){
                // Hapus kata "Bearer" dari token
                String token = bearerToken.substring(7);

                // verify token
                if(jwtService.verifyJwtToken(token)){
                    // claims token/ decode token
                    JwtClaims decodeJwt = jwtService.getClaimsByToken(token);

                    // find UserAccount by id form sub in token
                    Account userAccountBySub = userService.getByUserId(decodeJwt.getUserAccountId());
                    // verify Authentication use UserPassAuthToken
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userAccountBySub.getUsername(),
                            null,
                            userAccountBySub.getAuthorities()
                    );

                    // kita masukkan detail detail lain seperti ip addres, siapa yg ngehit
                    authentication.setDetails(new WebAuthenticationDetails(request));

                    // Set ini Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e){
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // ibarat finally
        // Lempar ke controller
        filterChain.doFilter(request,response);
    }
}

