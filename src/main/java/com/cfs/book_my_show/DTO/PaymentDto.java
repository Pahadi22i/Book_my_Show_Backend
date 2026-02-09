package com.cfs.book_my_show.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {


     private Long id;

     private String transactionId;

     private Double amount;

     private LocalDateTime paymentTime;

     private String paymentMethod;

     private String status;

    
}
