package com.cfs.book_my_show.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    
    private Long id;

    private String bookingNumber;

    private LocalDateTime bookingTime;

    private UserDto user;

    private ShowDto show;

    private String status;

    private double totalAmount;

    private List<ShowSeatDto> seat;


    private PaymentDto payment;



    
}
