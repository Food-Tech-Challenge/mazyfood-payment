package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;

public interface PaymentService {
    void processPayment(OrderPaymentDTO orderPaymentDTO);
    void updateOrderStatus(Integer orderId, String status);
}
