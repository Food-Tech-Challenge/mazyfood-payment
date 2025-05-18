package br.com.fiap.mazyfood.payment.dto;

public record OrderStatusDTO(
        Integer orderId,
        String paymentStatus
) {
}
