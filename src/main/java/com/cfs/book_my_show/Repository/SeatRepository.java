package com.cfs.book_my_show.Repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfs.book_my_show.Model.Seat;


@Repository
public interface SeatRepository extends JpaRepository<Seat ,Long> {
   

     List<Seat> findByScreenId(Long screenId);
    
     


}   
