package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;
import br.com.fiap.mazyfood.payment.service.impl.SQSListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SQSListenerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private SQSListener sqsListener;

    @Test
    void receiveMessageShouldProcessValidMessage() throws JsonProcessingException {
        String validMessage = "{\"orderId\":123,\"price\":20.90,\"paymentMethod\":\"credit_card\"}";
        OrderPaymentDTO orderPaymentDTO = new ObjectMapper().readValue(validMessage, OrderPaymentDTO.class);

        sqsListener.receiveMessage(validMessage);

        verify(paymentService).processPayment(orderPaymentDTO);
    }

    @Test
    void receiveMessageShouldThrowExceptionWhenMessageIsEmpty() {
        String emptyMessage = "";

        assertThrows(IllegalArgumentException.class, () -> sqsListener.receiveMessage(emptyMessage));
    }

    @Test
    void receiveMessageShouldThrowExceptionWhenMessageIsNull() {
        String nullMessage = null;

        assertThrows(IllegalArgumentException.class, () -> sqsListener.receiveMessage(nullMessage));
    }

    @Test
    void receiveMessageShouldThrowExceptionForInvalidJson() {
        String invalidMessage = "{\"orderId\":123,\"price\":20.90,\"paymentMethod\":}";

        assertThrows(JsonProcessingException.class, () -> sqsListener.receiveMessage(invalidMessage));
    }
}
