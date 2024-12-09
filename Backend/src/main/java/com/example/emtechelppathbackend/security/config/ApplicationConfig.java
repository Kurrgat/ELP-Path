package com.example.emtechelppathbackend.security.config;

import com.example.emtechelppathbackend.auditing.ApplicationAuditAware;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
//@EnableWebMvc
@RequiredArgsConstructor
public class ApplicationConfig {

@Autowired
    private final UsersRepository repository;

    @Bean
    public HostNameCapture hostNameCapture() {
        return new HostNameCapture();
    }

    @Bean
    public ServerPortService serverPortService() {
        return new ServerPortService();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findUsersByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
@Bean
public AuditorAware<Long>auditorAware(){
        return new ApplicationAuditAware();

}
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration)
    throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
