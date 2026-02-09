package com.cfs.book_my_show.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler; 
import org.springframework.web.context.request.WebRequest;



import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {


   @ExceptionHandler(ResourseNotFound.class)
   public ResponseEntity<ErrorResponse> resourseNotFoundException(ResourseNotFound ex, WebRequest request) {

      ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false));

  
      return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
   }


   @ExceptionHandler(SeatUnavailableExapction.class)
   public ResponseEntity<ErrorResponse> SeatUnavailableExapction(SeatUnavailableExapction ex, WebRequest request) {

      ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getDescription(false));

      return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> globalExceptionalHander(Exception ex, WebRequest request) {

      ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Bad Request",
            ex.getMessage(),
            request.getDescription(false));

      return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
   }
   
}


// correct code from chat ai
