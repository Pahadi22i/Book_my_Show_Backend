package com.cfs.book_my_show.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_Seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeat {


    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;


     @ManyToOne
     @JoinColumn(name = "show_id", nullable = false)
     private Show show;


     @ManyToOne
     @JoinColumn(name = "seat_id", nullable = false)
     private Seat seat;

     @Column(nullable = false)
     private String status;

     @Column(nullable = false)
     private Double price;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
     private Booking booking; 


}
