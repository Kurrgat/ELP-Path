package com.example.emtechelppathbackend.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/images/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

      @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }
}
