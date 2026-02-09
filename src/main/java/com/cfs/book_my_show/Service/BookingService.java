package com.cfs.book_my_show.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.cfs.book_my_show.DTO.BookingDto;
import com.cfs.book_my_show.DTO.BookingRequestDto;
import com.cfs.book_my_show.DTO.MovieDto;
import com.cfs.book_my_show.DTO.PaymentDto;
import com.cfs.book_my_show.DTO.ScreenDto;
import com.cfs.book_my_show.DTO.SeatDto;
import com.cfs.book_my_show.DTO.ShowDto;
import com.cfs.book_my_show.DTO.ShowSeatDto;
import com.cfs.book_my_show.DTO.TheaterDto;
import com.cfs.book_my_show.DTO.UserDto;
import com.cfs.book_my_show.Exceptions.ResourseNotFound;
import com.cfs.book_my_show.Exceptions.SeatUnavailableExapction;
import com.cfs.book_my_show.Model.Booking;
import com.cfs.book_my_show.Model.Payment;
import com.cfs.book_my_show.Model.Show;
import com.cfs.book_my_show.Model.ShowSeat;
import com.cfs.book_my_show.Model.User;
import com.cfs.book_my_show.Repository.BookingRepository;
import com.cfs.book_my_show.Repository.ShowRepository;
import com.cfs.book_my_show.Repository.ShowSeatRepository;
import com.cfs.book_my_show.Repository.UserRepository;

@Service
public class BookingService {

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private ShowRepository showRepository;

      @Autowired
      private ShowSeatRepository showSeatRepository;

      @Autowired
      private BookingRepository bookingRepository;

      // --- 1. CREATE BOOKING (Ticket Book Karna) ---
      // @Transactional ka matlab: "Ya toh sab kuch save hoga, ya kuch bhi nahi."
      // Agar Payment save ho gayi lekin Booking fail ho gayi, to Payment bhi wapas
      // roll-back ho jayegi.
      // Ye paise katne aur ticket na milne wali problem ko rokta hai.
      @Transactional
      public BookingDto createBooking(BookingRequestDto bookingRequest) {

            // Step 1: User ko dhundo (Kaun book kar raha hai?)
            User user = userRepository.findById(bookingRequest.getUserId())
                        .orElseThrow(() -> new ResourseNotFound("User Not found ..."));

            // Step 2: Show ko dhundo (Konsi movie aur time?)
            Show show = showRepository.findById(bookingRequest.getShowId())
                        .orElseThrow(() -> new ResourseNotFound("Show Not Found ..."));

            // Step 3: Jo seats user ne select ki hain, unhe database se nikalo.
            List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequest.getSeatId());

            // Step 4: CHECK AVAILABILITY (Sabse Zaroori Hissa)
            // Hum check kar rahe hain ki kya koi seat pehle se booked to nahi hai?
            for (ShowSeat seat : selectedSeats) {
                  if (!"AVAILABLE".equals(seat.getStatus())) {
                        // Agar ek bhi seat khali nahi hai, to error feko aur process rok do.
                        throw new SeatUnavailableExapction(
                                    "Seat " + seat.getSeat().getSeatNumber() + " is not available");
                  }
                  // Agar available hai, to temporarily "LOCKED" mark kar do taaki koi aur na le
                  // sake.
                  seat.setStatus("LOCKED");
            }
            // Seats ka naya status save karo.
            showSeatRepository.saveAll(selectedSeats);

            // Step 5: Total Price Calculate karo
            Double totalAmount = selectedSeats.stream()
                        .mapToDouble(ShowSeat::getPrice) // Har seat ka price nikalo
                        .sum(); // Sabko jodo

            // Step 6: Payment Record banao
            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaymentMethod(bookingRequest.getPaymentMethod());
            payment.setStatus("SUCCESS"); // Asli project mein ye Payment Gateway ke baad set hoga
            payment.setTransactionId(UUID.randomUUID().toString()); // Unique Transaction ID

            // Step 7: Booking Object banao (Ticket generate karna)
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setShow(show);
            booking.setBookingTime(LocalDateTime.now());
            booking.setStatus("CONFIRMED");
            booking.setTotalAmount(totalAmount);
            // Booking Number unique hona chahiye, isliye UUID use kiya.
            booking.setBookingNumber(UUID.randomUUID().toString());
            booking.setPayment(payment);

            // Booking ko database mein save karo
            Booking saveBooking = bookingRepository.save(booking);

            // Step 8: Seats ko Final "BOOKED" mark karo aur Booking se link karo
            selectedSeats.forEach(seat -> {
                  seat.setStatus("BOOKED");
                  seat.setBooking(saveBooking); // Seat ko batao ki wo kis booking ka hissa hai
            });

            // Seats ko wapas save karo updated status ke sath
            showSeatRepository.saveAll(selectedSeats);

            // User ko dikhane ke liye DTO return karo
            return mapToBookingDto(saveBooking, selectedSeats);
      }

      // --- 2. GET BOOKING BY NUMBER (Ticket Number se dhundo) ---
      public BookingDto getBookingByNumber(String bookingNumber) {
            // Pehle Booking table mein dhundo
            Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                        .orElseThrow(() -> new ResourseNotFound("Booking Not found ..."));

            // Ab ShowSeat table mein wo saari seats dhundo jo is booking se judi hain
            List<ShowSeat> seats = showSeatRepository.findAll()
                        .stream()
                        .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                        .collect(Collectors.toList());

            return mapToBookingDto(booking, seats);
      }

      // --- 3. GET BOOKING BY ID (Database ID se dhundo) ---
      public BookingDto getBookingById(Long id) {
            Booking booking = bookingRepository.findById(id)
                        .orElseThrow(() -> new ResourseNotFound("Booking Not found ..."));

            // Wahi same logic: Is booking ki seats filter karke nikalo
            List<ShowSeat> seats = showSeatRepository.findAll()
                        .stream()
                        .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                        .collect(Collectors.toList());
            return mapToBookingDto(booking, seats);
      }

      // --- 4. GET BOOKINGS BY USER (Ek user ki saari tickets) ---
      public List<BookingDto> getBooklingByUserId(Long userId) {
            // User ki saari bookings nikali
            List<Booking> bookings = bookingRepository.findByUserId(userId);

            // Har booking ke liye uski seats dhund kar DTO banaya
            return bookings.stream()
                        .map(booking -> {
                              List<ShowSeat> seats = showSeatRepository.findAll()
                                          .stream()
                                          .filter(seat -> seat.getBooking() != null
                                                      && seat.getBooking().getId().equals(booking.getId()))
                                          .collect(Collectors.toList());
                              return mapToBookingDto(booking, seats);
                        }).collect(Collectors.toList());
      }

      // --- 5. CANCEL BOOKING (Ticket Cancel karna) ---
      public BookingDto cancelBooking(Long id) {
            // Step 1: Booking dhundo
            Booking booking = bookingRepository.findById(id)
                        .orElseThrow(() -> new ResourseNotFound("Booking Not found ..."));

            // Step 2: Booking ka status CANCELLED karo
            booking.setStatus("CANCELLED"); // Spelling correction: CENCELLED -> CANCELLED

            // Step 3: Is booking ki saari seats dhundo
            List<ShowSeat> seats = showSeatRepository.findAll()
                        .stream()
                        .filter(seat -> seat.getBooking() != null
                                    && seat.getBooking().getId().equals(booking.getId()))
                        .collect(Collectors.toList());

            // Step 4: Har seat ko wapas "AVAILABLE" karo aur booking se unlink karo
            seats.forEach(seat -> {
                  seat.setStatus("AVAILABLE");
                  seat.setBooking(null); // Ab ye seat kisi ki nahi hai
            });

            // Step 5: Agar payment thi, to usko REFUNDED mark karo
            if (booking.getPayment() != null) {
                  booking.getPayment().setStatus("REFUNDED");
            }

            // Sab kuch database mein save karo
            Booking updateBooking = bookingRepository.save(booking);
            showSeatRepository.saveAll(seats);

            return mapToBookingDto(updateBooking, seats);
      }

      // --- 6. GET ALL BOOKINGS (Admin ke liye) ---
      public List<BookingDto> getAllBookings() {
            List<Booking> bookings = bookingRepository.findAll();
            return bookings.stream()
                        .map(booking -> {
                              List<ShowSeat> seats = showSeatRepository.findAll()
                                          .stream()
                                          .filter(seat -> seat.getBooking() != null
                                                      && seat.getBooking().getId().equals(booking.getId()))
                                          .collect(Collectors.toList());
                              return mapToBookingDto(booking, seats);
                        }).collect(Collectors.toList());
      }

      // --- 7. HELPER METHOD: Map Entity to DTO ---
      // Ye method Database object (Entity) ko JSON object (DTO) mein convert karta
      // hai.
      public BookingDto mapToBookingDto(Booking booking, List<ShowSeat> seats) {

            BookingDto bookingDto = new BookingDto();

            bookingDto.setId(booking.getId());
            // Note: Aapne yahan null set kiya hai, shayaad testing ke liye?
            // Real app mein: bookingDto.setBookingNumber(booking.getBookingNumber()); hona
            // chahiye.
            bookingDto.setBookingNumber(null);
            bookingDto.setBookingTime(booking.getBookingTime());
            bookingDto.setStatus(booking.getStatus());
            bookingDto.setTotalAmount(booking.getTotalAmount());

            // User ki details set karo
            UserDto userDto = new UserDto();
            userDto.setId(booking.getUser().getId());
            userDto.setName(booking.getUser().getName());
            userDto.setEmail(booking.getUser().getEmail());
            userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
            bookingDto.setUser(userDto);

            // Show ki details set karo
            ShowDto showDto = new ShowDto();
            showDto.setId(booking.getShow().getId());
            showDto.setStartTime(booking.getShow().getStartTime());
            showDto.setEndTime(booking.getShow().getEndTime());

            // Movie ki details
            MovieDto movieDto = new MovieDto();
            movieDto.setId(booking.getShow().getMovie().getId());
            movieDto.setTitle(booking.getShow().getMovie().getTitle());
            movieDto.setTitle(booking.getShow().getMovie().getDescription());
            movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
            movieDto.setGenre(booking.getShow().getMovie().getGenre());
            movieDto.setDurationMins(booking.getShow().getMovie().getDurationMint());
            movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
            movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
            showDto.setMovie(movieDto);

            // Screen aur Theater ki details
            ScreenDto screenDto = new ScreenDto();
            screenDto.setId(booking.getShow().getScreen().getId());
            screenDto.setName(booking.getShow().getScreen().getName());
            screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

            TheaterDto theaterDto = new TheaterDto();

            // **Important Fix Explanation:**
            // Pehle aap bookingDto.getShow() use kar rahe the, lekin wo abhi ban raha hai
            // (incomplete hai).
            // Isliye hume 'booking' (Entity) use karna chahiye jo database se aayi hai aur
            // jisme saara data hai.
            theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
            theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
            theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
            theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
            theaterDto.setTotalScreen(booking.getShow().getScreen().getTheater().getTotalScreen());

            screenDto.setTheater(theaterDto);
            showDto.setScreen(screenDto);
            bookingDto.setShow(showDto);

            // Seats ki details set karo
            List<ShowSeatDto> seatDtos = seats.stream()
                        .map(seat -> {
                              ShowSeatDto seatDto = new ShowSeatDto();
                              seatDto.setId(seat.getId());
                              seatDto.setStatus(seat.getStatus());
                              seatDto.setPrice(seat.getPrice());

                              SeatDto baseSeatDto = new SeatDto();
                              baseSeatDto.setId(seat.getSeat().getId());
                              baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                              baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                              baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                              seatDto.setSeat(baseSeatDto);
                              return seatDto;

                        })
                        .collect(Collectors.toList());
            bookingDto.setSeat(seatDtos); // Method name check kar lena (setSeat vs setSeats)

            // Payment details set karo
            if (booking.getPayment() != null) {
                  PaymentDto paymentDto = new PaymentDto();
                  paymentDto.setId(booking.getPayment().getId());
                  paymentDto.setAmount(booking.getPayment().getAmount());
                  paymentDto.setStatus(booking.getPayment().getStatus());
                  paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
                  paymentDto.setTransactionId(booking.getPayment().getTransactionId());

                  bookingDto.setPayment(paymentDto);
            }

            return bookingDto;
      }
}