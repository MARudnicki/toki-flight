package sg.toki.flight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sg.toki.flight.dto.BusinessFlightInfo;
import sg.toki.flight.dto.CheapFlightInfo;

import java.util.List;

/**
 * Created by tarunmalhotra on 1/18/19.
 */

@Service
public class FlightInfoRetrievalService {

	private static final String CHEAP_FLIGHT_PROVIDER_URL = "https://obscure-caverns-79008.herokuapp.com/cheap";
	private static final String BUSINESS_FLIGHT_PROVIDER_URL = "https://obscure-caverns-79008.herokuapp.com/business";

	private static final Logger log = LoggerFactory.getLogger(FlightInfoRetrievalService.class);

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public List<CheapFlightInfo> getCheapFlights() {
		ResponseEntity<List<CheapFlightInfo>> response = getRestTemplate().exchange(
			CHEAP_FLIGHT_PROVIDER_URL,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<CheapFlightInfo>>() {
			});
		return response.getBody();
	}

	public List<BusinessFlightInfo> getBusinessFlights() {
		ResponseEntity<List<BusinessFlightInfo>> response = getRestTemplate().exchange(
			BUSINESS_FLIGHT_PROVIDER_URL,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<BusinessFlightInfo>>() {
			});
		return response.getBody();
	}
}
