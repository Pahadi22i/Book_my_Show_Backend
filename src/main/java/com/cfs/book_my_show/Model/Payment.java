package com.cfs.book_my_show.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {


     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(nullable = false,unique = false )
     private String transactionId;

    @Column(nullable = false)
     private Double amount;

     @Column(nullable = false)
     private LocalDateTime paymentTime;
     
     @Column(nullable = false)
     private String paymentMethod;

     @Column(nullable = false)
     private String status;   // success , failed , pending

     @OneToOne(mappedBy = "payment")
     private Booking booking;
}
 