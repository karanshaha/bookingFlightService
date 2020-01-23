package com.aggregators.bookflight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FlightDataList {

  @JsonProperty("data")
  private List<FlightDetails> data;

  public List<FlightDetails> getData() {
    return data;
  }

  public void setData(List<FlightDetails> data) {
    this.data = data;
  }
}
