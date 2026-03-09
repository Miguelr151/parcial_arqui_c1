package com.iglesia.factory;

import com.iglesia.Payment;
import com.iglesia.PaymentType;

import java.math.BigDecimal;

public class PaymentFactory {

    public static Payment createPayment(PaymentType type, BigDecimal amount, Long referenceId) {

        Payment payment = new Payment();

        payment.setType(type);
        payment.setAmount(amount);
        payment.setReferenceId(referenceId);
        payment.setAttempts(0);

        return payment;
    }

}