package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.dto.GatewayReturnDTO;
import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;
import br.com.fiap.mazyfood.payment.dto.OrderStatusDTO;
import br.com.fiap.mazyfood.payment.entity.Payment;
import br.com.fiap.mazyfood.payment.repository.PaymentRepository;
import br.com.fiap.mazyfood.payment.service.impl.PaymentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private GatewayService gatewayService;

    @Mock
    private SqsTemplate sqsTemplate;

    private PaymentServiceImpl paymentService;


    @BeforeEach
    void setUp() {
        this.paymentService = new PaymentServiceImpl(gatewayService, paymentRepository, sqsTemplate, "payment-status");
    }


    @Test
    void processPaymentShouldSavePaymentAndSendStatusWhenPaymentIsSuccessful() throws JsonProcessingException {
        var orderPaymentDTO = new OrderPaymentDTO(123, new BigDecimal("20.90"), "credit_card");
        var payment = Payment.builder()
                .paymentMethod(orderPaymentDTO.paymentMethod())
                .amount(orderPaymentDTO.price())
                .orderId(orderPaymentDTO.orderId())
                .status("PAID")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .gateway("Mercado Pago")
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(gatewayService.processPayment(any(Payment.class))).thenReturn(new GatewayReturnDTO( UUID.randomUUID(),
                true));

        paymentService.processPayment(orderPaymentDTO);

        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(sqsTemplate).send(anyString(), anyString());
    }

    @Test
    void processPaymentShouldSavePaymentAndSendStatusWhenPaymentIsRejected() throws JsonProcessingException {
        var orderPaymentDTO = new OrderPaymentDTO(123, new BigDecimal("20.90"), "credit_card");
        var payment = Payment.builder()
                .paymentMethod(orderPaymentDTO.paymentMethod())
                .amount(orderPaymentDTO.price())
                .orderId(orderPaymentDTO.orderId())
                .status("PAID")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .gateway("Mercado Pago")
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(gatewayService.processPayment(any(Payment.class))).thenReturn(new GatewayReturnDTO( UUID.randomUUID(),
                false));

        paymentService.processPayment(orderPaymentDTO);

        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(sqsTemplate).send(anyString(), anyString());
    }

    @Test
    void updateOrderStatusShouldSendMessageToQueue() throws JsonProcessingException {
        var orderStatusDTO = new OrderStatusDTO(123, "PAID");
        String message = new ObjectMapper().writeValueAsString(orderStatusDTO);

        paymentService.updateOrderStatus(123, "PAID");

        verify(sqsTemplate).send(anyString(), eq(message));
    }

}
