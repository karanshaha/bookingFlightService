package com.aggregators.bookflight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CheapDataList {

  @JsonProperty("data")
  private List<CheapFlightDetails> data;

  public List<CheapFlightDetails> getData() {
    return data;
  }

  public void setData(List<CheapFlightDetails> data) {
    this.data = data;
  }
}
