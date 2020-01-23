package com.aggregators.bookflight.service;

import com.aggregators.bookflight.dto.CheapDataList;
import com.aggregators.bookflight.dto.CheapFlightDetails;
import com.aggregators.bookflight.dto.FlightDataList;
import com.aggregators.bookflight.dto.FlightDetails;
import com.aggregators.bookflight.dto.SearchResult;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class FlightServiceTest {

  @MockBean
  RestTemplate restTemplate;

  @Autowired
  FlightService flightService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() {
    reset(restTemplate);
  }

  @Test
  void getAllBusinessFlights() {
    List<FlightDetails> dummyBusinessFlightList = createDummyBusinessFlightList();

    FlightDataList flightDataList = new FlightDataList();
    flightDataList.setData(dummyBusinessFlightList);

    when(restTemplate.getForObject("https://tokigames-challenge.herokuapp.com/api/flights/business", FlightDataList.class))
      .thenReturn(flightDataList);

    List<FlightDetails> actualProcessedList = flightService.processDataAndFilterReturningBusinessFlights(dummyBusinessFlightList);
    assertEquals(2, actualProcessedList.size());
    assertEquals("Istanbul", actualProcessedList.get(0).getDeparture());
    assertEquals("Singapore", actualProcessedList.get(0).getArrival());
    assertEquals("Business", actualProcessedList.get(0).getFlightType());
  }

  @Test
  void getAllCheapFlights() {
    List<CheapFlightDetails> cheapFlightList = createDummyCheapFlightList();

    CheapDataList flightDataList = new CheapDataList();
    flightDataList.setData(cheapFlightList);

    when(restTemplate.getForObject("https://tokigames-challenge.herokuapp.com/api/flights/cheap", CheapDataList.class))
      .thenReturn(flightDataList);

    List<FlightDetails> actualProcessedList = flightService.processDataAndFilterReturningCheapFlights(cheapFlightList);
    assertEquals(2, actualProcessedList.size());
    assertEquals("Istanbul", actualProcessedList.get(0).getDeparture());
    assertEquals("Antalya", actualProcessedList.get(0).getArrival());
    assertEquals("Cheap", actualProcessedList.get(0).getFlightType());
  }

  @Test
  void getAllCheapFlights_NullPointer() {
    try {
      flightService.getAllCheapFlights();
    } catch (Exception e) {
      assertEquals(e.getClass(), NullPointerException.class);
    }

  }

  @Test
  void getAllBusinessFlights_NullPointer() {
    try {
      flightService.getAllBusinessFlights();
    } catch (Exception e) {
      assertEquals(e.getClass(), NullPointerException.class);
    }

  }

  @Test
  void processDataAndFilterReturningCheapFlights() {
    List<CheapFlightDetails> dummyCheapFlightList = createDummyCheapFlightList();

    List<FlightDetails> actualProcessedList = flightService.processDataAndFilterReturningCheapFlights(dummyCheapFlightList);
    assertEquals(2, actualProcessedList.size());
    assertEquals("Istanbul", actualProcessedList.get(0).getDeparture());
    assertEquals("Antalya", actualProcessedList.get(0).getArrival());
    assertEquals("Cheap", actualProcessedList.get(0).getFlightType());
  }

  @Test
  void processDataAndFilterReturningBusinessFlights() {
    List<FlightDetails> dummyBusinessFlightList = createDummyBusinessFlightList();

    List<FlightDetails> actualProcessedList = flightService.processDataAndFilterReturningBusinessFlights(dummyBusinessFlightList);
    assertEquals(2, actualProcessedList.size());
    assertEquals("Istanbul", actualProcessedList.get(0).getDeparture());
    assertEquals("Singapore", actualProcessedList.get(0).getArrival());
    assertEquals("Business", actualProcessedList.get(0).getFlightType());
  }

  @Test
  void getPaginatedSearchResult() {

    List<FlightDetails> dummyBusinessFlightList = createDummyBusinessFlightList();
    SearchResult paginatedSearchResult = flightService.getPaginatedSearchResult(1, dummyBusinessFlightList, 15);
    List<FlightDetails> finalList = paginatedSearchResult.getFlightDetails();
    assertEquals(4, finalList.size());
    assertEquals("Istanbul", finalList.get(0).getDeparture());
    assertEquals("Singapore", finalList.get(0).getArrival());

  }

  private List<CheapFlightDetails> createDummyCheapFlightList() {
    List<CheapFlightDetails> cheapFlightDetailsList = new ArrayList<>();
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1558902656", "1572169220", "Antalya-Istanbul"));
    cheapFlightDetailsList.add(buildCheapFlightWithDetails("1858902656", "1772169220", "Istanbul-Antalya"));
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