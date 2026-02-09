package com.cfs.book_my_show.Repository;



import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cfs.book_my_show.Model.Theater;




@Repository
public interface TheaterRepository extends JpaRepository< Theater,Long> {
   

    List<Theater> findByCity(String city);
    // List<Theater> findByShowId(String city);
}   
