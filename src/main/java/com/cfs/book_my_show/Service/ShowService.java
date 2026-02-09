
package com.cfs.book_my_show.Service;

import java.time.LocalDateTime;
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
import com.cfs.book_my_show.Model.Show;
import com.cfs.book_my_show.Model.ShowSeat;
import com.cfs.book_my_show.Repository.MovieRepository;
import com.cfs.book_my_show.Repository.ScreenRepository;
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

    public ShowDto createShow(ShowDto showDto) {

        Show show = new Show();

        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow((() -> new ResourseNotFound("Movie Not found ")));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow((() -> new ResourseNotFound("Screen Not found ")));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show saveShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(saveShow.getId(), "AVAILABLE");
        return mapToDto(saveShow, availableSeats);

    }

    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow((() -> new ResourseNotFound("Show Not found " + id)));
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, availableSeats);
    }


    public List<ShowDto> getAllShows(){
         List<Show> shows = showRepository.findAll();
               return shows.stream()
                       .map(show -> {
                       List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                                  return mapToDto(show, availableSeats);

                       }).collect(Collectors.toList());
    }


    public List<ShowDto> getShowByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);

                }).collect(Collectors.toList());
    }

     public List<ShowDto> getShowByMovieAndCity(Long movieId,String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_city(movieId,city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);

                }).collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByDateRange(LocalDateTime startDate ,LocalDateTime endDate) {
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, availableSeats);

                }).collect(Collectors.toList());
    }


    private ShowDto mapToDto (Show show, List<ShowSeat>availableSeats){
            
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovie(new MovieDto(
                   show.getMovie().getId(),
                   show.getMovie().getTitle(),
                   show.getMovie().getLanguage(),
                   show.getMovie().getDurationMint(),
                   show.getMovie().getPosterUrl(),
                   show.getMovie().getGenre(),
                   show.getMovie().getReleaseDate(),
                   show.getMovie().getDescription()
        ));
          
            TheaterDto theaterDto = new TheaterDto(
                      show.getScreen().getTheater().getId(),
                      show.getScreen().getTheater().getName(),
                      show.getScreen().getTheater().getAddress(),
                      show.getScreen().getTheater().getCity(),
                      show.getScreen().getTheater().getTotalScreen()
                      );


             showDto.setScreen(new ScreenDto(
                       show.getScreen().getId(),
                       show.getScreen().getName(),
                       show.getScreen().getTotalSeats(),

                       theaterDto
             )); 
             
           List<ShowSeatDto> seatDtos = availableSeats.stream()
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
                                  
                           }).collect(Collectors.toList());

                           showDto.setAvailableSeat(seatDtos); // ->in this line ai tell wrong code
                            // showDto.setSeat(seatDtos);
                                                               
                           return showDto;
                    
 
    }

}
