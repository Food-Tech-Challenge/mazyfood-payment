package br.com.fiap.mazyfood.payment.dto;

import java.math.BigDecimal;

public record OrderPaymentDTO(
        Integer orderId,
        BigDecimal price,
        String paymentMethod
) {
}