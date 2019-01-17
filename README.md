# TOKI-FLIGHT

A microservice which 
- Aggregates data from two disparate data sources [data source 1](https://obscure-caverns-79008.herokuapp.com/cheap) and [data source 2](https://obscure-caverns-79008.herokuapp.com/business)
- Exposes an API to 
-- Retrieve all the flight information
-- Filter flights based on some criterion


# Technologies used
- Java 8
- Maven
- Mongo DB
- Spring Boot

# Build the project!
- Import the maven project in an IDE of your choice
- To build and package the project using maven from the command line
`mvn clean package`

# Run the project!
- To run the spring boot application using the embedded tomcat from the command line
`java -jar target/flight-aggregator-1.0.0-SNAPSHOT.jar`

# How to use the search API

| Param Name | Description | Data Type|
| ------------- |-------------|-------------|
| page |Page number|int|
|departureCity| Departure city|String|
|arrivalCity|Arrival city|String|
|departureTime|Departure Time|int|
|arrivalTime|Arrival Time|int|
|flightType|Type of Flight - BUSINESS / CHEAP| String|
|sortDirection|ASC / DESC|String|
|sortBy|The property names - departureCity, arrivalCity, departureTime, arrivalTime, flightType| String|
|maximumSearchResults| Limit Search Results|int|