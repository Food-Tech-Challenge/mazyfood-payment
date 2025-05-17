package br.com.fiap.mazyfood.payment.service;


import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ch.qos.logback.core.util.StringUtil.isNullOrEmpty;

@Service
@RequiredArgsConstructor
public class SQSListener {

    private final PaymentService paymentService;

    @SqsListener("${events.queues.order-to-pay}")
    public void receiveMessage(String message) throws JsonProcessingException {

        if (isNullOrEmpty(message)) {
            throw new IllegalArgumentException("Message is empty!");
        }

        var orderPayment = new ObjectMapper().readValue(message, OrderPaymentDTO.class);

        this.paymentService.processPayment(orderPayment);
    }
}
