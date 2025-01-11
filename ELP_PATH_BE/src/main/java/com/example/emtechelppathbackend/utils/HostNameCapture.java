package com.example.emtechelppathbackend.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * HostNameCapture
 */
@Component
@Configuration
public class HostNameCapture {


@Autowired
ServerProperties serverProperties;



    @Bean
public java.lang.String getHost() throws UnknownHostException{
    String localhost = InetAddress.getLocalHost().getHostAddress();
    
    //running locally or remotely
    if("127.0.0.1".equals(localhost) || "localhost".equalsIgnoreCase(localhost)){
        return "http://localhost";
    } else {
//        return "http://"+localhost;
         return "http://"+"52.15.152.26";
    }
}

public String getPort() {
    return serverProperties.getPort().toString();
}

}