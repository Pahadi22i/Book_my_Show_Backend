package com.cfs.book_my_show.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    
   // hum service layer ko dto layer se connect kare taki hame sarvice layer me entity use nakarne padhe or entity expose na ho to save rahe ..
     private Long userId;

     private Long showId;

     private List<Long> seatId;

     private String paymentMethod;

}
