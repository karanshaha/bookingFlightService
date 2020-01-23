package com.aggregators.bookflight.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@JsonDeserialize
public class FlightDetails {

  String departure;
  String arrival;
  //@JsonProperty(access = Access.WRITE_ONLY)
  String departureTime;
  //@JsonProperty(access = Access.WRITE_ONLY)
  String arrivalTime;
  String flightType;

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }

  public String getArrival() {
    return arrival;
  }

  public void setArrival(String arrival) {
    this.arrival = arrival;
  }

  public void setDepartureTime(String departureTime) {
    this.departureTime = convertTimeInMillToDate(departureTime);
  }

  public void setArrivalTime(String arrivalTime) {
    this.arrivalTime = convertTimeInMillToDate(arrivalTime);
  }

  public String getFlightType() {
    return flightType;
  }

  public void setFlightType(String flightType) {
    this.flightType = flightType;
  }

  public String getDepartureTime() {
    return departureTime;
  }

  public String getArrivalTime() {
    return arrivalTime;
  }

  private String convertTimeInMillToDate(String time) {
    SimpleDateFormat formatter = new SimpleDateFormat(" dd MMM yyyy HH:mm:ss z");
    Calendar cal = Calendar.getInstance();
    if (time.contains(".")) {
      time = time.substring(0, time.indexOf("."));
    }
    cal.setTimeInMillis(Long.valueOf(time));
    return formatter.format(cal.getTime());

  }
}
