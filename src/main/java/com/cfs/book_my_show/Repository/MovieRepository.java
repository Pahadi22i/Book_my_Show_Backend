package com.cfs.book_my_show.Repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cfs.book_my_show.Model.Movie;

@Repository
public interface MovieRepository extends JpaRepository< Movie,Long> {
   

     List<Movie> findByLanguage(String language);
    
     List<Movie> findByGenre(String genre);

     List<Movie> findBytitleContaining(String title);
}   
