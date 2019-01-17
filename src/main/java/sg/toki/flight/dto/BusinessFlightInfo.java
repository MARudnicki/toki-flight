package sg.toki.flight.dto;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
public class BusinessFlightInfo {

	public static final String TYPE = "BUSINESS";

	private String uuid;
	private String flight;
	private String departure;
	private String arrival;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
}
