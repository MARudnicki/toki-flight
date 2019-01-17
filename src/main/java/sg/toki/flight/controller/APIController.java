package sg.toki.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sg.toki.flight.dto.SearchParams;
import sg.toki.flight.model.Flight;
import sg.toki.flight.service.FlightQueryService;

import java.util.List;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
@Controller
public class APIController {

	@Autowired
	private FlightQueryService flightQueryService;

	@RequestMapping(method = RequestMethod.GET, value="/api/v1/flights")
	@ResponseBody
	public List<Flight> getFlights() {
		return flightQueryService.getAllFlights();
	}

	@RequestMapping(method = RequestMethod.POST, value="/api/v1/flights/search", consumes = "application/json")
	@ResponseBody
	public List<Flight> searchFlights(@RequestBody SearchParams searchParams) {
		return flightQueryService.searchFlights(searchParams);
	}


}
