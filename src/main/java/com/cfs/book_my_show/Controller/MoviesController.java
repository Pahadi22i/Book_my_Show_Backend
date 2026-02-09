// package com.cfs.book_my_show.Controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;


// import com.cfs.book_my_show.DTO.MovieDto;
// import com.cfs.book_my_show.Service.MovieService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/movies")
// public class MoviesController {
    
//     @Autowired
//      private MovieService movieService;


//      @PostMapping
//     public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto){
//          return new ResponseEntity<>(movieService.createMovie(movieDto),HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<MovieDto> getMovieById(@PathVariable long id){
//         return ResponseEntity.ok(movieService.getMovieById(id));
//     }


//     @GetMapping
//     public ResponseEntity< List<MovieDto >>getMovieById() {
//         return ResponseEntity.ok(movieService.getAllMovies());
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cfs.book_my_show.DTO.MovieDto;
import com.cfs.book_my_show.Service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    // 1. Create Movie
    // URL: POST http://localhost:8081/api/movies
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) {
        return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }

    // 2. Get Movie By ID
    // URL: GET http://localhost:8081/api/movies/1
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // 3. Get All Movies
    // URL: GET http://localhost:8081/api/movies
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        // FIX: Method name was 'getMovieById' in your code, changed to 'getAllMovies'
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // 4. Update Movie
    // URL: PUT http://localhost:8081/api/movies/1
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    // 5. Delete Movie
    // URL: DELETE http://localhost:8081/api/movies/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie Deleted Successfully with id: " + id);
    }

    // 6. Get Movies by Language
    // URL: GET http://localhost:8081/api/movies/language/Hindi
    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieDto>> getMoviesByLanguage(@PathVariable String language) {
        return ResponseEntity.ok(movieService.getMovieByLanguage(language));
    }

    // 7. Get Movies by Genre
    // URL: GET http://localhost:8081/api/movies/genre/Action
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(movieService.getMovieGenre(genre));
    }

    // 8. Search Movies by Title
    // URL: GET http://localhost:8081/api/movies/search?title=Avengers
    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMoviesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.getMovieTitle(title));
    }
}