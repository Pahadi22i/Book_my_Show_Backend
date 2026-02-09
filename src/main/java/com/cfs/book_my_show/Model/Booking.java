package com.cfs.book_my_show.Model;

import java.time.LocalDateTime;
import java.util.List;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column( nullable = false, unique = true)
     private String  bookingNumber;

     @Column(nullable = false )
     private LocalDateTime bookingTime;

     @ManyToOne
     @JoinColumn(name = "user_id",nullable = false)
     private User user;

     @ManyToOne
     @JoinColumn(name = "show_id", nullable = false)
     private Show show; 

     @Column(nullable = false)
     private String status;  // confome , cencel, pending

    @Column(nullable = false)
     private Double totalAmount;

     @OneToMany(mappedBy = "booking",cascade = CascadeType.ALL)
     private List<ShowSeat> showSeat;

     @OneToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "payment_id")
     private Payment payment;


} 
