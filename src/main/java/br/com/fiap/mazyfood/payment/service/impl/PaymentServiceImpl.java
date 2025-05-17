package br.com.fiap.mazyfood.payment.service.impl;

import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;
import br.com.fiap.mazyfood.payment.dto.OrderStatusDTO;
import br.com.fiap.mazyfood.payment.entity.Payment;
import br.com.fiap.mazyfood.payment.repository.PaymentRepository;
import br.com.fiap.mazyfood.payment.service.GatewayService;
import br.com.fiap.mazyfood.payment.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final GatewayService gatewayService;
    private final PaymentRepository paymentRepository;
    private final SqsTemplate sqsTemplate;
    private final String orderToPayQueue;

    @Autowired
    public PaymentServiceImpl(GatewayService gatewayService,
                               PaymentRepository paymentRepository,
                               SqsTemplate sqsTemplate,
                               @Value("${events.queues.payment-status}") String orderToPayQueue) {
        this.gatewayService = gatewayService;
        this.paymentRepository = paymentRepository;
        this.sqsTemplate = sqsTemplate;
        this.orderToPayQueue = orderToPayQueue;
    }

    @Override
    public void processPayment(OrderPaymentDTO orderPaymentDTO) {

        var payment = this.paymentRepository.save(Payment.builder()
                .paymentMethod(orderPaymentDTO.paymentMethod())
                .amount(orderPaymentDTO.price())
                .orderId(orderPaymentDTO.orderId())
                .status("PAID")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .gateway("Mercado Pago")
                .build());


        var paymentMercadoPago = this.gatewayService.processPayment(payment);

        if (paymentMercadoPago.status()) {
            payment.setStatus("PAID");
        } else {
            payment.setStatus("REJECTED");
        }


        this.paymentRepository.save(payment);

        this.updateOrderStatus(orderPaymentDTO.orderId(), payment.getStatus());

    }

    @Override
    public void updateOrderStatus(Integer orderId, String status) {

        try {

            var message = new OrderStatusDTO(orderId, status);

            this.sqsTemplate.send(this.orderToPayQueue, this.objectToString(message));

        }catch (JsonProcessingException e) {
            System.out.printf("Error converting object to string: %s%n", e.getMessage());
        }

    }

    private String objectToString(Object object) throws JsonProcessingException {

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.writeValueAsString(object);
    }
}
