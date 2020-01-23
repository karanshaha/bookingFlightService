package com.aggregators.bookflight.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FlightBookingExceptionHandler extends  RuntimeException {

  public FlightBookingExceptionHandler(String exception){
    super(exception);

  }
}