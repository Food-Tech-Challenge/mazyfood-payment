package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.entity.Payment;
import br.com.fiap.mazyfood.payment.service.impl.MercadoPagoGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MercadoPagoGatewayServiceTest {

    @InjectMocks
    private MercadoPagoGatewayService mercadoPagoGatewayService;

    @Test
    void processPaymentShouldReturnSuccessfulGatewayReturnWhenPaymentIsValid() {
        Payment payment = Payment.builder()
                .orderId(123)
                .amount(new BigDecimal("20.90"))
                .paymentMethod("credit_card")
                .build();

        var result = mercadoPagoGatewayService.processPayment(payment);

        assertNotNull(result);
        assertTrue(result.status());
        assertNotNull(result.id());
    }

}
