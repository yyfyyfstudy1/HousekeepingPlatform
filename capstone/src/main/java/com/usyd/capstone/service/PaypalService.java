package com.usyd.capstone.service;

import com.paypal.api.payments.Payment;



public interface PaypalService {

    Payment createPayment(Integer taskId, String cancelUrl, String successUrl);


    Payment executePayment(String paymentId, String payerId);

}