package br.com.fiap.mazyfood.payment.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@Document(collection = "payment")
public class Payment {

    @Id
    private String id;
    private Integer orderId;
    private BigDecimal amount;
    private String status;
    private String gateway;
    private String paymentMethod;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}