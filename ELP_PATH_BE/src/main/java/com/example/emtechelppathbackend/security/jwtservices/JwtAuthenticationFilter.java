package com.example.emtechelppathbackend.security.jwtservices;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Configuration
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader= request.getHeader("Authorization");
        log.info("#######The header is {}",authHeader);
        final String jwtToken;
        final String userEmail;
        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;//break time!
        }
        jwtToken = authHeader.substring(7); //extract token from authorization head
        userEmail = jwtService.extractUsername(jwtToken);

        //after implementing extraction methods in the JwtService class,2do extract the useremail from JWT token
        if (userEmail!= null && SecurityContextHolder.getContext().getAuthentication()==null) //making sure the user is not already authenticated
        {
            log.info("#####Email is {}", userEmail);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //we do not have any user details yet
                        userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // updating security context holder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //call the filter chain, handing over the filter for the next filtering
        filterChain.doFilter(request, response);
    }
}
