// package com.cfs.book_my_show.Controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.cfs.book_my_show.DTO.BookingDto;
// import com.cfs.book_my_show.DTO.BookingRequestDto;
// import com.cfs.book_my_show.Service.BookingService;

// import jakarta.validation.Valid;


// @RestController
// @RequestMapping("/api/booking")
// public class BookingController {
//      @Autowired
//     private BookingService bookingService;

//     @PostMapping
//     public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
//             return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto), HttpStatus.CREATED);
//     }
//    @GetMapping("/{id}")
//     public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id){
//         return ResponseEntity.ok(bookingService.getBookingById(id));
//     }


    
// }



package com.cfs.book_my_show.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cfs.book_my_show.DTO.BookingDto;
import com.cfs.book_my_show.DTO.BookingRequestDto;
import com.cfs.book_my_show.Service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // 1. Create a new Booking
    // Endpoint: POST /api/bookings
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequest) {
        BookingDto bookingDto = bookingService.createBooking(bookingRequest);
        return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
    }

    // 2. Get Booking by ID
    // Endpoint: GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // 3. Get Booking by Booking Number (UUID String)
    // Endpoint: GET /api/bookings/number/{bookingNumber}
    @GetMapping("/number/{bookingNumber}")
    public ResponseEntity<BookingDto> getBookingByNumber(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(bookingService.getBookingByNumber(bookingNumber));
    }

    // 4. Get All Bookings (This is what you asked for)
    // Endpoint: GET /api/bookings
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // 5. Get Bookings by User ID
    // Endpoint: GET /api/bookings/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBooklingByUserId(userId));
    }

    // 6. Cancel Booking
    // Endpoint: DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}