package sg.toki.flight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sg.toki.flight.dto.BusinessFlightInfo;
import sg.toki.flight.dto.CheapFlightInfo;
import sg.toki.flight.model.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
@Service
public class FlightAggregatorService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void addCheapFlights(List<CheapFlightInfo> cheapFlights) {
		for (CheapFlightInfo flightInfo: cheapFlights) {
			Flight flight = new Flight();
			flight.setIdentifier(String.valueOf(flightInfo.getId()));
			flight.setArrivalCity(flightInfo.getArrival());
			flight.setDepartureCity(flightInfo.getDeparture());
			flight.setArrivalTime(flightInfo.getArrivalTime());
			flight.setDepartureTime(flightInfo.getArrivalTime());
			flight.setFlightType(CheapFlightInfo.TYPE);
			mongoTemplate.save(flight);
		}

	}

	public void addBusinessFlights(List<BusinessFlightInfo> businessFlights) {
		for (BusinessFlightInfo flightInfo: businessFlights) {
			Flight flight = new Flight();
			flight.setIdentifier(flightInfo.getUuid());
			String flightName = flightInfo.getFlight(); // This is of the format departure -> arrival
			String[] cities = Arrays.stream(flightName.split("->"))
				.map(String::trim)
				.toArray(String[]::new);
			flight.setArrivalCity(cities[1]);
			flight.setDepartureCity(cities[0]);
			flight.setArrivalTime(getTimeInMilliSeconds(flightInfo.getArrival()));
			flight.setDepartureTime(getTimeInMilliSeconds(flightInfo.getDeparture()));
			flight.setFlightType(BusinessFlightInfo.TYPE);
			mongoTemplate.save(flight);

		}
	}

	private long getTimeInMilliSeconds(String dateValue) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			return formatter.parse(dateValue).getTime();
		} catch (ParseException e) {
			return 0L; // Not the best place to do this. For now this would do.
		}
	}
}
