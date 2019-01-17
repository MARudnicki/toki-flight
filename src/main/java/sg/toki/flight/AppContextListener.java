package sg.toki.flight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sg.toki.flight.dto.BusinessFlightInfo;
import sg.toki.flight.dto.CheapFlightInfo;
import sg.toki.flight.service.FlightAggregatorService;
import sg.toki.flight.service.FlightInfoRetrievalService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private FlightInfoRetrievalService flightInfoRetrievalService;

	@Autowired
	private FlightAggregatorService flightAggregatorService;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		List<CheapFlightInfo> cheapFlights = flightInfoRetrievalService.getCheapFlights();
		log.info(String.format("%s cheap flights returned", cheapFlights.size()));
		flightAggregatorService.addCheapFlights(cheapFlights);


		List<BusinessFlightInfo> businessFlights = flightInfoRetrievalService.getBusinessFlights();
		log.info(String.format("%s business flights returned", businessFlights.size()));
		flightAggregatorService.addBusinessFlights(businessFlights);
	}
}
