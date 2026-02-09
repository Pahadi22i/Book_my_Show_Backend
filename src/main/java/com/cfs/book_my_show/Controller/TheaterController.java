package com.cfs.book_my_show.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cfs.book_my_show.DTO.TheaterDto;
import com.cfs.book_my_show.Service.TheaterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    // 1. Create Theater
    // URL: POST http://localhost:8081/api/theaters
    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@Valid @RequestBody TheaterDto theaterDto) {
        return new ResponseEntity<>(theaterService.createTheater(theaterDto), HttpStatus.CREATED);
    }

    // 2. Get All Theaters
    // URL: GET http://localhost:8081/api/theaters
    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    // 3. Get Theater By ID
    // URL: GET http://localhost:8081/api/theaters/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }

    // 4. Get Theaters By City
    // URL: GET http://localhost:8081/api/theaters/city/{city}
    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDto>> getTheatersByCity(@PathVariable String city) {
        return ResponseEntity.ok(theaterService.getAllTheaterByCity(city));
    }

    // 5. Update Theater
    // URL: PUT http://localhost:8081/api/theaters/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterDto theaterDto) {
        return ResponseEntity.ok(theaterService.updateTheater(id, theaterDto));
    }

    // 6. Delete Theater
    // URL: DELETE http://localhost:8081/api/theaters/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok("Theater deleted successfully with id: " + id);
    }
}