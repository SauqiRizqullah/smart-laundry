package com.gruptiga.smartlaundry.security;

import com.gruptiga.smartlaundry.dto.response.JwtClaims;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.repository.TokenRepository;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTH_HEADER);
        try {
            if (token != null) {
                if (tokenRepository.findByToken(token).isPresent()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;                }
                // Verifikasi token
                if (jwtService.verifyJwtToken(token)) {
                    // Dekode klaim token
                    JwtClaims decodeJwt = jwtService.getClaimsByToken(token);
                    // Temukan UserAccount berdasarkan ID dari klaim token
                    Account userAccountBySub = userService.getByUserId(decodeJwt.getUserAccountId());
                    // Buat token autentikasi
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userAccountBySub.getUsername(),
                            null,
                            userAccountBySub.getAuthorities()                    );
                    // Set detail autentikasi, seperti IP address
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set autentikasi di Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;        }
        // Lanjutkan ke filter berikutnya
        filterChain.doFilter(request, response);
    }
}
