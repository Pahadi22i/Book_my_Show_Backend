package com.cfs.book_my_show.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfs.book_my_show.Model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking ,Long> {
   

     List<Booking> findByUserId(Long userId);
    
     Optional<Booking> findByBookingNumber(String bookingNumber);

     List<Booking> findByShowId(Long id);
}   
