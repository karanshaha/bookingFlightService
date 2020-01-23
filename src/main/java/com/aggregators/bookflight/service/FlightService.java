package com.aggregators.bookflight.service;

import com.aggregators.bookflight.Exception.FlightBookingExceptionHandler;
import com.aggregators.bookflight.dto.CheapDataList;
import com.aggregators.bookflight.dto.FlightDataList;
import com.aggregators.bookflight.dto.FlightDetails;
import com.aggregators.bookflight.dto.CheapFlightDetails;
import com.aggregators.bookflight.dto.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class FlightService {

  @Autowired
  RestTemplate restTemplate;

  public List<FlightDetails> getAllBusinessFlights() {
    String uri = "https://tokigames-challenge.herokuapp.com/api/flights/business";
    FlightDataList result = null;
    try {
      result = restTemplate.getForObject(uri, FlightDataList.class);
    } catch (Exception e) {
      throw new FlightBookingExceptionHandler("External API failure.");
    }
    return processDataAndFilterReturningBusinessFlights(result.getData());
  }

  public List<FlightDetails> getAllCheapFlights() {
    String uri = "http://tokigames-challenge.herokuapp.com/api/flights/cheap";
    CheapDataList result;
    try {
      result = restTemplate.getForObject(uri, CheapDataList.class);
    } catch (Exception e) {
      throw new FlightBookingExceptionHandler("External API failure.");
    }

    return processDataAndFilterReturningCheapFlights(result.getData());
  }

  /**
   * This method is used to take response for cheap flights and convert into generic Object also it filters and returns only the returning cheap
   * flights.
   *
   * @return @List<{@link FlightDetails}
   */
  public List<FlightDetails> processDataAndFilterReturningCheapFlights(List<CheapFlightDetails> data) {
    List<FlightDetails> returningFights = new ArrayList<>();
    for (CheapFlightDetails cheapFlight : data) {
      List<String> SourceDestinationList = Collections.list(new StringTokenizer(cheapFlight.getRoute(), "-")).stream()
        .map(token -> (String) token)
        .collect(Collectors.toList());
      FlightDetails flightDetails = new FlightDetails();
      flightDetails.setFlightType("Cheap");
      flightDetails.setArrival(SourceDestinationList.get(0));
      flightDetails.setDeparture(SourceDestinationList.get(1));
      flightDetails.setDepartureTime(cheapFlight.getDeparture());
      flightDetails.setArrivalTime(cheapFlight.getArrival());
      returningFights.add(flightDetails);
    }
    return processDataAndFilterReturningBusinessFlights(returningFights);
  }

  /**
   * This method is used to take response for business flights and convert into generic Object also it filters and returns only the returning cheap
   * flights.
   *
   * @return @List<{@link FlightDetails}
   */
  public List<FlightDetails> processDataAndFilterReturningBusinessFlights(List<FlightDetails> flightDetailsLst) {
    List<FlightDetails> returningFights = new ArrayList<>();
    if (!CollectionUtils.isEmpty(flightDetailsLst)) {

      for (FlightDetails bussFlight : flightDetailsLst) {
        for (FlightDetails bussFlightToCompare : flightDetailsLst) {
          if (bussFlight.getDeparture().equals(bussFlightToCompare.getArrival()) && bussFlight.getArrival()
            .equals(bussFlightToCompare.getDeparture())) {
            if (bussFlight.getFlightType() == null) {
              bussFlight.setFlightType("Business");
            }
            returningFights.add(bussFlight);
          }
        }
      }
    }
    return returningFights;
  }

  /**
   * This method returns the paginated data as per the request.
   */
  public SearchResult getPaginatedSearchResult(Integer page, List<FlightDetails> flightDetails, Integer itemsPerPage) {

    int start = (page - 1) * itemsPerPage + 1;
    int totalItems = flightDetails.size();
    int end = totalItems;
    int total_pages = (int) Math.ceil((double) flightDetails.size() / itemsPerPage);
    SearchResult searchResult = new SearchResult();
    if (page > 0 && page <= total_pages) {
      if (itemsPerPage < totalItems) {
        end = itemsPerPage * page;
        if (end > totalItems) {
          end = totalItems;
        }
      }

      List<FlightDetails> paginatedResult = flightDetails.subList(start - 1, end);
      List<FlightDetails> sorted = paginatedResult.stream()
        .sorted(Comparator.comparing(FlightDetails::getFlightType).thenComparing(FlightDetails::getDepartureTime))
        .collect(Collectors.toList());

      searchResult.setPer_page(itemsPerPage);
      searchResult.setTotal(totalItems);
      searchResult.setPage(page);
      searchResult.setTotal_pages(total_pages);
      searchResult.setFlightDetails(sorted);
      return searchResult;
    }

    return searchResult;
  }
}
