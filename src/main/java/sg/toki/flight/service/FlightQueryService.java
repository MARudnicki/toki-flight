package sg.toki.flight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import sg.toki.flight.dto.SearchParams;
import sg.toki.flight.model.Flight;

import java.util.List;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
@Service
public class FlightQueryService {

	private static final int MAX_SEARCH_RESULTS = 10;
	private static final String DEFAULT_SORT_BY = "departureCity";

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<Flight> getAllFlights() {
		return mongoTemplate.findAll(Flight.class);
	}

	public List<Flight> searchFlights(SearchParams searchParams) {
		Sort.Direction direction = Sort.DEFAULT_DIRECTION;
		if (searchParams.getSortDirection() != null) {
			try {
				direction = Sort.Direction.valueOf(searchParams.getSortDirection());
			} catch (Exception e) {
			}
		}

		String sortBy = searchParams.getSortBy() == null ? DEFAULT_SORT_BY : searchParams.getSortBy();

		int maxSearchResults = searchParams.getMaximumSearchResults() == 0 ? MAX_SEARCH_RESULTS : searchParams.getMaximumSearchResults();

		Pageable pageable = PageRequest.of(searchParams.getPage(), maxSearchResults, Sort.by(direction, sortBy));
		Query flightQuery = new Query().with(pageable);

		if (searchParams.getDepartureCity() != null) {
			flightQuery.addCriteria(Criteria.where("departureCity").is(searchParams.getDepartureCity()));
		}

		if (searchParams.getArrivalCity() != null) {
			flightQuery.addCriteria(Criteria.where("arrivalCity").is(searchParams.getArrivalCity()));
		}

		// We can go as granular as we want for the Date Time comparison
		if (searchParams.getArrivalTime() != null) {
			flightQuery.addCriteria(Criteria.where("arrivalTime").is(searchParams.getArrivalTime()));
		}

		// We can go as granular as we want for the Date Time comparison
		if (searchParams.getDepartureTime() != null) {
			flightQuery.addCriteria(Criteria.where("departureTime").is(searchParams.getDepartureTime()));
		}

		if (searchParams.getFlightType() != null) {
			flightQuery.addCriteria(Criteria.where("flightType").is(searchParams.getFlightType()));
		}

		return mongoTemplate.find(flightQuery, Flight.class);
	}
}
