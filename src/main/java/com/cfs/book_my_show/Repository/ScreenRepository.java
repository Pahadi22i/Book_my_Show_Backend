package com.cfs.book_my_show.Repository;






import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfs.book_my_show.Model.Screen;




@Repository
public interface ScreenRepository extends JpaRepository<Screen ,Long> {

    Optional<Screen>findByTheaterId (Long theaterId );
}   
