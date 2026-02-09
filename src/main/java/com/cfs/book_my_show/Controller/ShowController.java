package com.cfs.book_my_show.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cfs.book_my_show.DTO.ShowDto;
import com.cfs.book_my_show.Service.ShowService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    // 1. Create a new Show
    // URL: POST http://localhost:8081/api/shows
    @PostMapping
    public ResponseEntity<ShowDto> createShow(@Valid @RequestBody ShowDto showDto) {
        return new ResponseEntity<>(showService.createShow(showDto), HttpStatus.CREATED);
    }

    // 2. Get Show by ID
    // URL: GET http://localhost:8081/api/shows/1
    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    // 3. Get All Shows
    // URL: GET http://localhost:8081/api/shows
    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    // 4. Get Shows by Movie ID
    // URL: GET http://localhost:8081/api/shows/movie/1
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowByMovie(movieId));
    }

    // 5. Get Shows by Movie ID and City
    // URL: GET http://localhost:8081/api/shows/search?movieId=1&city=Mumbai
    @GetMapping("/search")
    public ResponseEntity<List<ShowDto>> getShowsByMovieAndCity(
            @RequestParam Long movieId,
            @RequestParam String city) {
        return ResponseEntity.ok(showService.getShowByMovieAndCity(movieId, city));
    }

    // 6. Get Shows by Date Range
    // URL: GET
    // http://localhost:8081/api/shows/date-range?startDate=2023-10-01T10:00:00&endDate=2023-10-01T20:00:00
    @GetMapping("/date-range")
    public ResponseEntity<List<ShowDto>> getShowsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(showService.getShowsByDateRange(startDate, endDate));
    }
}