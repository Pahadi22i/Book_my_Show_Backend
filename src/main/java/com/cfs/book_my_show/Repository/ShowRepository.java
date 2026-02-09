package com.cfs.book_my_show.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfs.book_my_show.Model.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show ,Long> {
   

     List<Show> findByMovieId(Long movie_id);
    
     Optional<Show> findByScreenId(Long screenId);  //@ correct by ai Optional<Show> findByScreen(Long screenId);

     List<Show> findByStartTimeBetween(LocalDateTime stat,LocalDateTime end);

     List<Show> findByMovie_IdAndScreen_Theater_city(Long movieId, String city);
}   
