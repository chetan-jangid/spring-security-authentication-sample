package com.security.authentication.configuration.security.filter;

import com.security.authentication.configuration.security.SecurityUserDetailsService;
import com.security.authentication.persistence.entity.UserAccount;
import com.security.authentication.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("Processing request: {}", request.getRequestURI());
        if (SecurityContextHolder.getContext().getAuthentication() == null &&
                !request.getRequestURI().contains("account/authenticate")) {
            final String token = TokenUtils.extractToken(request);
            if (StringUtils.hasLength(token)) {
                log.info("Processing token: {}", token);
                this.processToken(request, token);
            }
        }

        chain.doFilter(request, response);
    }

    private void processToken(HttpServletRequest request, String token) {
        String username = TokenUtils.getUsername(token);
        UserDetails userDetails = this.securityUserDetailsService.loadUserByUsername(username);
        UserAccount userAccount = this.securityUserDetailsService.userAccount(username);

        if (TokenUtils.validate(token, userDetails) && StringUtils.hasLength(userAccount.getTokenIdentifier())) {
            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
