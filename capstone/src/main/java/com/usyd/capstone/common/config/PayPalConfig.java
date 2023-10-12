package com.usyd.capstone.common.config;


import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {
    @Value("${paypal.mode}")
    private String mode;

    @Value("${paypal.client.app}")
    private String clientID;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext apiContext = new APIContext(clientID,clientSecret,mode);
        return apiContext;
    }



}
