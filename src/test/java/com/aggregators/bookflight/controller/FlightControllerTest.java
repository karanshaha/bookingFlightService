package com.aggregators.bookflight.controller;

import com.aggregators.bookflight.dto.CheapDataList;
import com.aggregators.bookflight.dto.CheapFlightDetails;
import com.aggregators.bookflight.dto.FlightDataList;
import com.aggregators.bookflight.dto.FlightDetails;
import com.aggregators.bookflight.dto.SearchResult;
import com.aggregators.bookflight.service.FlightService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FlightController.class)
class FlightControllerTest {

  @MockBean
  FlightService flightService;

  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() {
    reset(flightService);
  }

  @Test
  void getAggregationForAll() throws Exception {

    String uri = "http://localhost:8080/api/aggregation?flightType=All";
    String expectedResponse = "{\"page\":1,\"per_page\":15,\"total\":2,\"total_pages\":1,\"data\":[{\"departure\":\"Istanbul\",\"arrival\":\"Singapore\",\"departureTime\":\" 19 Jan 1970 06:31:42 IST\",\"arrivalTime\":\" 19 Jan 1970 06:31:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Istanbul\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Greece\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Paris\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"}]}";

    List<FlightDetails> flightDetails = createDummyBusinessFlightList();
    FlightDataList flightDataList = new FlightDataList();
    flightDataList.setData(flightDetails);

    List<FlightDetails> finalList = new ArrayList<>();
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));

    List<CheapFlightDetails> cheapFlightList = createDummyCheapFlightList();
    CheapDataList cheapDataList = new CheapDataList();
    cheapDataList.setData(cheapFlightList);

    when(flightService.getAllBusinessFlights()).thenReturn(finalList);

    when(flightService.getAllCheapFlights()).thenReturn(finalList);

    SearchResult searchResult = new SearchResult();

    searchResult.setPage(1);
    searchResult.setPer_page(15);
    searchResult.setTotal_pages(1);
    searchResult.setFlightDetails(flightDetails);
    searchResult.setTotal(2);

    when(flightService.getPaginatedSearchResult(1, finalList, 15)).thenReturn(searchResult);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getAggregationForCheap() throws Exception {

    String uri = "http://localhost:8080/api/aggregation?flightType=cheap";
    String expectedResponse = "{\"page\":1,\"per_page\":15,\"total\":2,\"total_pages\":1,\"data\":[{\"departure\":\"Istanbul\",\"arrival\":\"Singapore\",\"departureTime\":\" 19 Jan 1970 06:31:42 IST\",\"arrivalTime\":\" 19 Jan 1970 06:31:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Istanbul\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Greece\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Paris\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"}]}";

    List<FlightDetails> flightDetails = createDummyBusinessFlightList();
    FlightDataList flightDataList = new FlightDataList();
    flightDataList.setData(flightDetails);

    List<FlightDetails> finalList = new ArrayList<>();
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));

    List<CheapFlightDetails> cheapFlightList = createDummyCheapFlightList();
    CheapDataList cheapDataList = new CheapDataList();
    cheapDataList.setData(cheapFlightList);

    when(flightService.getAllCheapFlights()).thenReturn(finalList);

    SearchResult searchResult = new SearchResult();

    searchResult.setPage(1);
    searchResult.setPer_page(15);
    searchResult.setTotal_pages(1);
    searchResult.setFlightDetails(flightDetails);
    searchResult.setTotal(2);

    when(flightService.getPaginatedSearchResult(1, finalList, 15)).thenReturn(searchResult);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());

  }

  @Test
  void getAggregationForBusiness() throws Exception {

    String uri = "http://localhost:8080/api/aggregation?flightType=Business";
    String expectedResponse = "{\"page\":1,\"per_page\":15,\"total\":2,\"total_pages\":1,\"data\":[{\"departure\":\"Istanbul\",\"arrival\":\"Singapore\",\"departureTime\":\" 19 Jan 1970 06:31:42 IST\",\"arrivalTime\":\" 19 Jan 1970 06:31:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Istanbul\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Greece\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"},{\"departure\":\"Singapore\",\"arrival\":\"Paris\",\"departureTime\":\" 21 Jan 1970 14:05:02 IST\",\"arrivalTime\":\" 22 Jan 1970 17:51:42 IST\",\"flightType\":\"Business\"}]}";

    List<FlightDetails> flightDetails = createDummyBusinessFlightList();
    FlightDataList flightDataList = new FlightDataList();
    flightDataList.setData(flightDetails);

    List<FlightDetails> finalList = new ArrayList<>();
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));
    finalList.add(flightDetails.get(0));
    finalList.add(flightDetails.get(1));

    when(flightService.getAllBusinessFlights()).thenReturn(finalList);

    SearchResult searchResult = new SearchResult();

    searchResult.setPage(1);
    searchResult.setPer_page(15);
    searchResult.setTotal_pages(1);
    searchResult.setFlightDetails(flightDetails);
    searchResult.setTotal(2);

    when(flightService.getPaginatedSearchResult(1, finalList, 15)).thenReturn(searchResult);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());

  }

  @Test
  void getAggregationForInvalidFilter() throws Exception {

    String uri = "http://localhost:8080/api/aggregation?flightType=None";

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals("Operation Supports only Business / Cheap /All as input .", mvcResult.getResponse().getContentAsString());

  }

  private List<CheapFlightDetails> createDummyCheapFlightList() {
    List<CheapFlightDetails> cheapFlightDetailsList = new ArrayList<>();
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1558902656", "1572169220", "Singapore-Istanbul"));
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1858902656", "1772169220", "Istanbul-Singapore"));
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1858902656", "1772169220", "Paris-France"));
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1858902656", "1772169220", "Paris-Greece"));
    return cheapFlightDetailsList;
  }

  private CheapFlightDetails buildCheapFlightWithDetails(String departure, String arrival, String route) {
    CheapFlightDetails cheapFlightDetails = new CheapFlightDetails();
    cheapFlightDetails.setRoute(route);
    cheapFlightDetails.setDeparture(departure);
    cheapFlightDetails.setArrival(arrival);
    return cheapFlightDetails;
  }

  private List<FlightDetails> createDummyBusinessFlightList() {
    List<FlightDetails> flightDetailsLst = new ArrayList<>();
    flightDetailsLst.add(buildFlightWithDetails("Business", "Istanbul", "Singapore", "1558902656.000000000", "1558902656.000000000"));
    flightDetailsLst.add(buildFlightWithDetails("Business", "Singapore", "Istanbul", "1758902656.000000000", "1858902656.000000000"));
    flightDetailsLst.add(buildFlightWithDetails("Business", "Singapore", "Greece", "1758902656.000000000", "1858902656.000000000"));
    flightDetailsLst.add(buildFlightWithDetails("Business", "Singapore", "Paris", "1758902656.000000000", "1858902656.000000000"));

    return flightDetailsLst;
  }

  private FlightDetails buildFlightWithDetails(String flightType, String departure, String arrival, String departTime, String arrivalTime) {
    FlightDetails flightDetails = new FlightDetails();
    flightDetails.setFlightType(flightType);
    flightDetails.setDeparture(departure);
    flightDetails.setArrival(arrival);
    flightDetails.setDepartureTime(departTime);
    flightDetails.setArrivalTime(arrivalTime);

    return flightDetails;
  }
}