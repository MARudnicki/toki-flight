package sg.toki.flight.dto;

/**
 * Created by tarunmalhotra on 1/18/19.
 */
public class SearchParams {

	private int page;
	private String departureCity;
	private String arrivalCity;
	private String departureTime;
	private String arrivalTime;
	private String flightType;
	private String sortDirection;
	private String sortBy;
	private int maximumSearchResults;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getFlightType() {
		return flightType;
	}

	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		if (sortBy != "ASC" || sortBy != "DESC") {
			sortBy = null;
		}
		this.sortBy = sortBy;
	}

	public int getMaximumSearchResults() {
		return maximumSearchResults;
	}

	public void setMaximumSearchResults(int maximumSearchResults) {
		this.maximumSearchResults = maximumSearchResults;
	}
}
