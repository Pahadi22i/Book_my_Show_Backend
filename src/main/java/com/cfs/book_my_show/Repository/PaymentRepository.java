package com.cfs.book_my_show.Repository;






import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cfs.book_my_show.Model.Payment;




@Repository
public interface PaymentRepository extends JpaRepository< Payment,Long> {
   

    Optional<Payment> findByTransactionId(String transactionId);
}   
