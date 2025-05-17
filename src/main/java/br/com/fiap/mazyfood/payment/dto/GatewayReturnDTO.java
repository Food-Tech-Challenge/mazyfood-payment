package br.com.fiap.mazyfood.payment.dto;

import java.util.UUID;

public record GatewayReturnDTO(
        UUID id,
        Boolean status
) {
}