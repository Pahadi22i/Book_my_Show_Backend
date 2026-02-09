package com.cfs.book_my_show.Repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfs.book_my_show.Model.ShowSeat;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat ,Long> {
   

     List<ShowSeat> findByShowId(Long movie_id);
    
     List<ShowSeat> findByShowIdAndStatus(Long showId,String status);


}   
