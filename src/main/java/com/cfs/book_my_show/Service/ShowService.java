
package com.cfs.book_my_show.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfs.book_my_show.DTO.MovieDto;
import com.cfs.book_my_show.DTO.ScreenDto;
import com.cfs.book_my_show.DTO.SeatDto;
import com.cfs.book_my_show.DTO.ShowDto;
import com.cfs.book_my_show.DTO.ShowSeatDto;
import com.cfs.book_my_show.DTO.TheaterDto;
import com.cfs.book_my_show.Exceptions.ResourseNotFound;
import com.cfs.book_my_show.Model.Movie;
import com.cfs.book_my_show.Model.Screen;
import com.cfs.book_my_show.Model.Seat; // Physical Seat Model
import com.cfs.book_my_show.Model.Show;
import com.cfs.book_my_show.Model.ShowSeat;
import com.cfs.book_my_show.Repository.MovieRepository;
import com.cfs.book_my_show.Repository.ScreenRepository;
import com.cfs.book_my_show.Repository.SeatRepository; // Screen Seats lane ke liye
import com.cfs.book_my_show.Repository.ShowRepository;
import com.cfs.book_my_show.Repository.ShowSeatRepository;

@Service
public class ShowService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private SeatRepository seatRepository; // 🔥 Iski zaroorat hai Screen seats lane ke liye

    // --- 1. CREATE SHOW (Logic Corrected) ---
    public ShowDto createShow(ShowDto showDto) {

        // 1. Basic Show Details Set Karo
        Show show = new Show();

        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow((() -> new ResourseNotFound("Movie Not found ")));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow((() -> new ResourseNotFound("Screen Not found ")));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        // 2. Show ko Save karo (ID generate ho jayegi)
        Show savedShow = showRepository.save(show);

        // 3. 🔥 MAGIC STEP: Screen ki sari Physical Seats nikalo
        List<Seat> physicalSeats = seatRepository.findByScreenId(screen.getId());

        // 4. Har Physical Seat ke liye ek ShowSeat banao (AVAILABLE status ke sath)
        List<ShowSeat> showSeats = new ArrayList<>();

        for (Seat seat : physicalSeats) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeat(seat); // Link physical seat
            showSeat.setShow(savedShow); // Link current show
            showSeat.setStatus("AVAILABLE"); // 🔥 Default Status
            showSeat.setPrice(seat.getBasePrice()); // Default Price (Screen wala)

            showSeats.add(showSeat);
        }

        // 5. Saari ShowSeats ko DB me save karo
        List<ShowSeat> savedShowSeats = showSeatRepository.saveAll(showSeats);

        // 6. DTO Return karo (Jisme ab sari seats hongi)
        return mapToDto(savedShow, savedShowSeats);
    }

    // --- 2. GET SHOW BY ID ---
    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow((() -> new ResourseNotFound("Show Not found " + id)));

        // Yaha hum Show ki SAARI seats layenge (Available + Booked)
        List<ShowSeat> allSeats = showSeatRepository.findByShowId(show.getId());

        return mapToDto(show, allSeats);
    }

    // --- 3. GET ALL SHOWS ---
    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream().map(show -> {
            // Performance ke liye aap chahein to yaha seats na bhejein (empty list)
            List<ShowSeat> seats = showSeatRepository.findByShowId(show.getId());
            return mapToDto(show, seats);
        }).collect(Collectors.toList());
    }

    // --- 4. OTHER GET METHODS ---
    public List<ShowDto> getShowByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream().map(show -> {
            List<ShowSeat> seats = showSeatRepository.findByShowId(show.getId());
            return mapToDto(show, seats);
        }).collect(Collectors.toList());
    }

    public List<ShowDto> getShowByMovieAndCity(Long movieId, String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_city(movieId, city);
        return shows.stream().map(show -> {
            List<ShowSeat> seats = showSeatRepository.findByShowId(show.getId());
            return mapToDto(show, seats);
        }).collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream().map(show -> {
            List<ShowSeat> seats = showSeatRepository.findByShowId(show.getId());
            return mapToDto(show, seats);
        }).collect(Collectors.toList());
    }

    // --- MAPPER FUNCTION ---
    private ShowDto mapToDto(Show show, List<ShowSeat> showSeatList) {
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        // Map Movie
        if (show.getMovie() != null) {
            showDto.setMovie(new MovieDto(
                    show.getMovie().getId(),
                    show.getMovie().getTitle(),
                    show.getMovie().getLanguage(),
                    show.getMovie().getDurationMint(),
                    show.getMovie().getPosterUrl(),
                    show.getMovie().getGenre(),
                    show.getMovie().getReleaseDate(),
                    show.getMovie().getDescription()));
        }

        // Map Screen & Theater
        if (show.getScreen() != null) {
            TheaterDto theaterDto = new TheaterDto(
                    show.getScreen().getTheater().getId(),
                    show.getScreen().getTheater().getName(),
                    show.getScreen().getTheater().getAddress(),
                    show.getScreen().getTheater().getCity(),
                    show.getScreen().getTheater().getTotalScreen());

            showDto.setScreen(new ScreenDto(
                    show.getScreen().getId(),
                    show.getScreen().getName(),
                    show.getScreen().getTotalSeats(),
                    theaterDto));
        }

        // Map Seats (ShowSeat -> ShowSeatDto)
        List<ShowSeatDto> seatDtos = showSeatList.stream().map(showSeat -> {
            ShowSeatDto seatDto = new ShowSeatDto();
            seatDto.setId(showSeat.getId());
            seatDto.setStatus(showSeat.getStatus());
            seatDto.setPrice(showSeat.getPrice());

            SeatDto baseSeatDto = new SeatDto();
            baseSeatDto.setId(showSeat.getSeat().getId());
            baseSeatDto.setSeatNumber(showSeat.getSeat().getSeatNumber());
            baseSeatDto.setSeatType(showSeat.getSeat().getSeatType());
            baseSeatDto.setBasePrice(showSeat.getSeat().getBasePrice());

            seatDto.setSeat(baseSeatDto);
            return seatDto;
        }).collect(Collectors.toList());

        // 🔥 CRITICAL: Yeh aapke DTO ke field name se match kar raha hai
        showDto.setAvailableSeat(seatDtos);

        return showDto;
    }
}