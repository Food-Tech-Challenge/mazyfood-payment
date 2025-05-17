package br.com.fiap.mazyfood.payment.service;

import br.com.fiap.mazyfood.payment.dto.OrderPaymentDTO;
import br.com.fiap.mazyfood.payment.entity.Payment;
import br.com.fiap.mazyfood.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(OrderPaymentDTO orderPaymentDTO) {

        this.paymentRepository.save(Payment.builder()
                        .paymentMethod(orderPaymentDTO.paymentMethod())
                        .amount(orderPaymentDTO.price())
                        .orderId(orderPaymentDTO.orderId())
                        .status("PAID")
                        .createdAt(LocalDate.now())
                        .updatedAt(LocalDate.now())
                        .gateway("Mercado Pago")
                .build());


        System.out.printf("Processing payment for order ID: %d%n", orderPaymentDTO.orderId());

    }
}
