package com.cfs.book_my_show.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourseNotFound extends RuntimeException{


       public ResourseNotFound (String message){
            super(message);
       }
    
}
 