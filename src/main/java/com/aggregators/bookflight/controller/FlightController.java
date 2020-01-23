package com.aggregators.bookflight.controller;

import com.aggregators.bookflight.dto.FlightDetails;
import com.aggregators.bookflight.dto.SearchResult;
import com.aggregators.bookflight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "Flight aggregators", description = "Operations pertaining to Flights")
public class FlightController {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  FlightService flightService;

  @Value("${flight.type.business}")
  String businessType;

  @Value("${flight.type.cheap}")
  String cheapType;

  @Value("${flight.type.all}")
  String allType;

  @ApiOperation(value = "View a list of available returning flights sorted as per Flight type and then Departure time", response = ResponseEntity.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully retrieved list"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })

  @GetMapping("/aggregation")
  public @ResponseBody
  ResponseEntity<?> getAggregation(
    @ApiParam(value = "To filter and get the flight by as per Business/Cheap/all") @RequestParam(name = "flightType") String flightType,
    @ApiParam(value = "Page no that is currently displayed") @RequestParam(defaultValue = "1") Integer page,
    @ApiParam(value = "No of results per page") @RequestParam(defaultValue = "15") Integer itemsPerPage) throws IOException {
    List<FlightDetails> flightDetails = new ArrayList<>();
    List<FlightDetails> allCheapFlights = new ArrayList<>();

    if (flightType.equalsIgnoreCase(businessType)) {
      flightDetails = flightService.getAllBusinessFlights();
    } else if (flightType.equalsIgnoreCase(cheapType)) {
      allCheapFlights = flightService.getAllCheapFlights();
    } else if (flightType.equalsIgnoreCase(allType)) {
      flightDetails = flightService.getAllBusinessFlights();
      allCheapFlights = flightService.getAllCheapFlights();
    } else {
      return new ResponseEntity("Operation Supports only Business / Cheap /All as input .", HttpStatus.OK);
    }
    flightDetails.addAll(allCheapFlights);
    SearchResult paginatedSearchResult = flightService.getPaginatedSearchResult(page, flightDetails, itemsPerPage);

    return new ResponseEntity<>(paginatedSearchResult, HttpStatus.OK);
  }
}
