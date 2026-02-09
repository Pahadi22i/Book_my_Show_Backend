package com.cfs.book_my_show.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfs.book_my_show.DTO.MovieDto;
import com.cfs.book_my_show.Exceptions.ResourseNotFound;
import com.cfs.book_my_show.Model.Movie;
import com.cfs.book_my_show.Repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto){
        Movie movie = maptoEntity(movieDto);
        Movie saveMovie = movieRepository.save(movie);
        return mapToDto(saveMovie);
    }

    private MovieDto mapToDto(Movie movie){
         MovieDto movieDto = new MovieDto();
         movieDto.setId(movie.getId());
         movieDto.setTitle(movie.getTitle());
         movieDto.setDescription(movie.getDescription());
         movieDto.setLanguage(movie.getLanguage());
         movieDto.setGenre(movie.getGenre());
         movieDto.setDurationMins(movie.getDurationMint());
         movieDto.setReleaseDate(movie.getReleaseDate());
         movieDto.setPosterUrl(movie.getPosterUrl());

         return movieDto;

    }

    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                    .orElseThrow((()-> new ResourseNotFound("Movie Not found with id : " + id)));

                    return mapToDto(movie);
    }
    public List<MovieDto> getAllMovies(){
        List<Movie> movies = movieRepository.findAll();
                 return movies.stream()
                        .map(this:: mapToDto)
                        .collect(Collectors.toList());            
    }

    public List<MovieDto> getMovieByLanguage(String language){
        List <Movie> movies= movieRepository.findByLanguage(language);
               return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<MovieDto> getMovieGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenre(genre);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieTitle(String title) {
        List<Movie> movies = movieRepository.findBytitleContaining(title);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id,MovieDto movieDto){
        Movie movie = movieRepository.findById(id)
                    .orElseThrow((() -> new ResourseNotFound("Movie Not found with id : " + id)));
            movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDurationMint(movieDto.getDurationMins());
        movie.setLanguage(movieDto.getLanguage());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setGenre(movieDto.getGenre());
        movie.setPosterUrl(movieDto.getPosterUrl());  
        
      Movie updatedMovie = movieRepository.save(movie);
      return mapToDto(updatedMovie);
    }

    public void deleteMovie(Long id){
        Movie movie = movieRepository.findById(id)
                    .orElseThrow((() -> new ResourseNotFound("Movie Not found with id : " + id)));
                    movieRepository.delete(movie);

    }


    public Movie maptoEntity(MovieDto movieDto){
        Movie movie= new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDurationMint(movieDto.getDurationMins());
        movie.setLanguage(movieDto.getLanguage());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setGenre(movieDto.getGenre());
        movie.setPosterUrl(movieDto.getPosterUrl());


        return movie;
        
    }
    
}

