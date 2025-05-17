package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.dto.GatewayReturnDTO;
import br.com.fiap.mazyfood.payment.entity.Payment;

public interface GatewayService {
    GatewayReturnDTO processPayment(Payment payment);
}
