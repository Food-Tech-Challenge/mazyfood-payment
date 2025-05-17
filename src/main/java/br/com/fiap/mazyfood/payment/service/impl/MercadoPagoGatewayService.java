package br.com.fiap.mazyfood.payment.service.impl;

import br.com.fiap.mazyfood.payment.dto.GatewayReturnDTO;
import br.com.fiap.mazyfood.payment.entity.Payment;
import br.com.fiap.mazyfood.payment.service.GatewayService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MercadoPagoGatewayService implements GatewayService {

    @Override
    public GatewayReturnDTO processPayment(Payment payment) {

        // Aqui você pode implementar a lógica para processar o pagamento com o Mercado Pago

        return new GatewayReturnDTO(
                UUID.randomUUID(),
                true
        );
    }
}
