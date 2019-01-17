package sg.toki.flight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sg.toki.flight.controller.APIController;
import sg.toki.flight.dto.BusinessFlightInfo;
import sg.toki.flight.dto.CheapFlightInfo;
import sg.toki.flight.dto.SearchParams;
import sg.toki.flight.model.Flight;
import sg.toki.flight.service.FlightAggregatorService;
import sg.toki.flight.service.FlightQueryService;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	private static final String CHEAP_FLIGHT_DATA = "[{\"id\":2428990950790504448,\"departure\":\"Chajari\",\"arrival\":\"Chacarita\",\"departureTime\":1547747236107,\"arrivalTime\":1547749661288},{\"id\":2960671767825359872,\"departure\":\"Hilario Ascasubi\",\"arrival\":\"Huanchillas\",\"departureTime\":1547741180424,\"arrivalTime\":1547749267383},{\"id\":8018093875805380608,\"departure\":\"Algiers\",\"arrival\":\"28 de Noviembre\",\"departureTime\":1547742467657,\"arrivalTime\":1547745432644},{\"id\":4709230374987837440,\"departure\":\"Mendiolaza\",\"arrival\":\"Bordj\",\"departureTime\":1547749480137,\"arrivalTime\":1547752737524},{\"id\":1004373199874688000,\"departure\":\"Bordj\",\"arrival\":\"Presidente Derqui\",\"departureTime\":1547750315027,\"arrivalTime\":1547756459077},{\"id\":3639152003502545920,\"departure\":\"Jesus Maria\",\"arrival\":\"Obera\",\"departureTime\":1547742641352,\"arrivalTime\":1547743223342},{\"id\":9032893109833165824,\"departure\":\"San Francisco Solano\",\"arrival\":\"Portena\",\"departureTime\":1547746824936,\"arrivalTime\":1547753726863},{\"id\":2120023933366032384,\"departure\":\"Vuelta de Obligado\",\"arrival\":\"Rivadavia\",\"departureTime\":1547741950464,\"arrivalTime\":1547749833911},{\"id\":5631301892127988736,\"departure\":\"Santo Tome\",\"arrival\":\"Floresta\",\"departureTime\":1547749926506,\"arrivalTime\":1547750149207},{\"id\":2811164579204956160,\"departure\":\"General Las Heras\",\"arrival\":\"Alberti\",\"departureTime\":1547743728515,\"arrivalTime\":1547747657280},{\"id\":1378037592884528128,\"departure\":\"Jose Marmol\",\"arrival\":\"Plaza Huincul\",\"departureTime\":1547742016417,\"arrivalTime\":1547742469081},{\"id\":1635875976087853056,\"departure\":\"Beccar\",\"arrival\":\"Santo Tome\",\"departureTime\":1547743857571,\"arrivalTime\":1547744704057},{\"id\":3255239557246270464,\"departure\":\"San Antonio de Obligado\",\"arrival\":\"Azazga\",\"departureTime\":1547744092522,\"arrivalTime\":1547751681621},{\"id\":3738129137079862272,\"departure\":\"La Boca\",\"arrival\":\"Cipolletti\",\"departureTime\":1547742000088,\"arrivalTime\":1547750959805},{\"id\":172639546174635008,\"departure\":\"Villa Dolores\",\"arrival\":\"Doblas\",\"departureTime\":1547743349989,\"arrivalTime\":1547744380840},{\"id\":199947037450296320,\"departure\":\"Sarmiento\",\"arrival\":\"San Salvador de Jujuy\",\"departureTime\":1547748675343,\"arrivalTime\":1547754323522},{\"id\":5827343130402171904,\"departure\":\"Pellegrini\",\"arrival\":\"Barrio Fisherton\",\"departureTime\":1547748654023,\"arrivalTime\":1547752660436},{\"id\":2400923972739387392,\"departure\":\"Alberti\",\"arrival\":\"Chlef\",\"departureTime\":1547740856972,\"arrivalTime\":1547743254463},{\"id\":7902812031248308224,\"departure\":\"Berazategui\",\"arrival\":\"Famailla\",\"departureTime\":1547740803248,\"arrivalTime\":1547746743453},{\"id\":2622854875830094848,\"departure\":\"Yerba Buena\",\"arrival\":\"Tupungato\",\"departureTime\":1547744255699,\"arrivalTime\":1547752734578},{\"id\":6957597472009174016,\"departure\":\"San Antonio Oeste\",\"arrival\":\"La Boca\",\"departureTime\":1547746997792,\"arrivalTime\":1547756620555},{\"id\":8509459474213082112,\"departure\":\"Maquinista Savio\",\"arrival\":\"Crespo\",\"departureTime\":1547742951301,\"arrivalTime\":1547743278822},{\"id\":7842190722292198400,\"departure\":\"Villa Angela\",\"arrival\":\"Gobernador Galvez\",\"departureTime\":1547741426148,\"arrivalTime\":1547746781691},{\"id\":6149118535295507456,\"departure\":\"Rafaela\",\"arrival\":\"Rada Tilly\",\"departureTime\":1547749537882,\"arrivalTime\":1547757710613},{\"id\":4032276183183865856,\"departure\":\"Acebal\",\"arrival\":\"Canada Rosquin\",\"departureTime\":1547746384878,\"arrivalTime\":1547751130911},{\"id\":4002289044920006656,\"departure\":\"Asamblea\",\"arrival\":\"Ranchos\",\"departureTime\":1547741446516,\"arrivalTime\":1547747984693},{\"id\":6795880432180544512,\"departure\":\"General Acha\",\"arrival\":\"Villa Gesell\",\"departureTime\":1547740845208,\"arrivalTime\":1547741779096},{\"id\":7829833602966238208,\"departure\":\"San Miguel\",\"arrival\":\"General Pico\",\"departureTime\":1547741609315,\"arrivalTime\":1547745064671},{\"id\":7397061974802164736,\"departure\":\"Ranelagh\",\"arrival\":\"San Gregorio\",\"departureTime\":1547745880592,\"arrivalTime\":1547748714368},{\"id\":445267812021530624,\"departure\":\"Manantial\",\"arrival\":\"Tigre\",\"departureTime\":1547744128997,\"arrivalTime\":1547744227494},{\"id\":2652300252582800384,\"departure\":\"Tupungato\",\"arrival\":\"San Antonio de Padua\",\"departureTime\":1547748869290,\"arrivalTime\":1547754074948},{\"id\":2419772478680662016,\"departure\":\"Hernando\",\"arrival\":\"La Madrid\",\"departureTime\":1547748118007,\"arrivalTime\":1547750092137},{\"id\":4324027416095270912,\"departure\":\"Monteros\",\"arrival\":\"Bariloche\",\"departureTime\":1547740886052,\"arrivalTime\":1547748234237},{\"id\":6250132433351402496,\"departure\":\"Open Door\",\"arrival\":\"Don Torcuato\",\"departureTime\":1547745371256,\"arrivalTime\":1547752214752},{\"id\":1258453585243810816,\"departure\":\"Warnes\",\"arrival\":\"Chacabuco\",\"departureTime\":1547744196930,\"arrivalTime\":1547751032860},{\"id\":3080712404437349376,\"departure\":\"Concepcion\",\"arrival\":\"Bandera\",\"departureTime\":1547750036540,\"arrivalTime\":1547756417563}]";

	private static final String BUSINESS_FLIGHT_DATA = "[{\"uuid\":\"d7a14b3d-a6c0-4509-96c1-64a669343260\",\"flight\":\"Comodoro Rivadavia -> Humahuaca\",\"departure\":\"2019-01-17T18:32:09.150Z\",\"arrival\":\"2019-01-17T20:54:13.467Z\"},{\"uuid\":\"7a30b9a1-fc39-4be0-a19c-c9d7da7f74b2\",\"flight\":\"Viamonte -> Villa Luzuriaga\",\"departure\":\"2019-01-17T16:11:21.635Z\",\"arrival\":\"2019-01-17T16:38:14.384Z\"},{\"uuid\":\"84abddba-9de9-4b02-a492-3f45d6dd0f57\",\"flight\":\"Souk Ahras -> Salsipuedes\",\"departure\":\"2019-01-17T16:22:00.726Z\",\"arrival\":\"2019-01-17T17:38:48.561Z\"},{\"uuid\":\"525f4213-595e-4cec-9c3a-dbd754e1ae84\",\"flight\":\"Santo Tome -> Rada Tilly\",\"departure\":\"2019-01-17T16:42:25.546Z\",\"arrival\":\"2019-01-17T18:24:31.328Z\"},{\"uuid\":\"7b8f18d2-84e9-4d3e-a4be-32618b14e6af\",\"flight\":\"Villa Giardino -> Ezpeleta\",\"departure\":\"2019-01-17T17:36:27.610Z\",\"arrival\":\"2019-01-17T20:06:55.175Z\"},{\"uuid\":\"8628f4ce-be13-4f88-93a4-adf0576347b7\",\"flight\":\"Roldan -> Nogoya\",\"departure\":\"2019-01-17T18:35:13.645Z\",\"arrival\":\"2019-01-17T20:46:09.120Z\"},{\"uuid\":\"d5b5b655-23c8-4f6e-909e-1851c5239455\",\"flight\":\"Albardon -> Resistencia\",\"departure\":\"2019-01-17T16:20:57.685Z\",\"arrival\":\"2019-01-17T16:47:20.125Z\"},{\"uuid\":\"39c5ce42-dc83-40f7-b5dc-9aa29da0f866\",\"flight\":\"Diego de Alvear -> Sanchez\",\"departure\":\"2019-01-17T18:46:56.254Z\",\"arrival\":\"2019-01-17T19:17:38.621Z\"},{\"uuid\":\"d9194936-fb04-45ee-926a-8525667ce702\",\"flight\":\"Palmira -> Concepcion\",\"departure\":\"2019-01-17T18:47:43.968Z\",\"arrival\":\"2019-01-17T20:48:05.219Z\"},{\"uuid\":\"d052f918-dd87-47f2-aa52-7e9b043a4df8\",\"flight\":\"San Antonio de Areco -> Hilario Ascasubi\",\"departure\":\"2019-01-17T18:33:46.278Z\",\"arrival\":\"2019-01-17T20:35:28.939Z\"},{\"uuid\":\"f5a2cd63-7557-42f4-a490-608a2c8c53c7\",\"flight\":\"La Falda -> 28 de Noviembre\",\"departure\":\"2019-01-17T16:50:08.973Z\",\"arrival\":\"2019-01-17T18:38:55.081Z\"},{\"uuid\":\"4548cace-34cb-4f7c-99d0-746ab5c53257\",\"flight\":\"San Guillermo -> Retiro\",\"departure\":\"2019-01-17T18:02:16.067Z\",\"arrival\":\"2019-01-17T18:16:55.724Z\"},{\"uuid\":\"644b1b5b-0921-4c8d-bdc0-8aa995dbc8ee\",\"flight\":\"Mar de Ajo -> Rosario\",\"departure\":\"2019-01-17T18:06:04.765Z\",\"arrival\":\"2019-01-17T19:01:33.849Z\"}]";


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private APIController controller;

	@Autowired
	private FlightQueryService flightQueryService;

	@Autowired
	private FlightAggregatorService flightAggregatorService;

	@PostConstruct
	public void init() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<CheapFlightInfo> cheapFlights = mapper.readValue(CHEAP_FLIGHT_DATA, new TypeReference<List<CheapFlightInfo>>(){});
		flightAggregatorService.addCheapFlights(cheapFlights);

		List<BusinessFlightInfo> businessFlights = mapper.readValue(BUSINESS_FLIGHT_DATA, new TypeReference<List<BusinessFlightInfo>>(){});
		flightAggregatorService.addBusinessFlights(businessFlights);
	}

	@After
	public void destroy() {
		mongoTemplate.dropCollection(Flight.class);
	}

	@Test
	public void testForFlightRecordsCount() {
		assertThat(flightQueryService.getAllFlights().size()).isEqualTo(49);
	}

	@Test
	public void testController() {
		assertThat(controller).isNotNull();
	}

	@Test
	public void testAllFlights() throws Exception {
		this.mockMvc.perform(get("/api/v1/flights"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(49)));

	}


	@Test
	public void testSearchForCheapFlights() throws Exception {
		SearchParams searchParams = new SearchParams();
		searchParams.setFlightType(CheapFlightInfo.TYPE);
		searchParams.setMaximumSearchResults(100);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchJSONString = objectMapper.writeValueAsString(searchParams);
		this.mockMvc.perform(post("/api/v1/flights/search")
			.contentType(MediaType.APPLICATION_JSON)
			.content(searchJSONString)
			.characterEncoding("utf-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(36)));

	}

	@Test
	public void testSearchForBusinessFlights() throws Exception {
		SearchParams searchParams = new SearchParams();
		searchParams.setFlightType(BusinessFlightInfo.TYPE);
		searchParams.setMaximumSearchResults(100);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchJSONString = objectMapper.writeValueAsString(searchParams);
		this.mockMvc.perform(post("/api/v1/flights/search")
			.contentType(MediaType.APPLICATION_JSON)
			.content(searchJSONString)
			.characterEncoding("utf-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(13)));

	}

	@Test
	public void testSearchForCheapFlightsAndArrivalCity() throws Exception {
		SearchParams searchParams = new SearchParams();
		searchParams.setFlightType(CheapFlightInfo.TYPE);
		searchParams.setArrivalCity("Chacarita");
		searchParams.setMaximumSearchResults(100);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchJSONString = objectMapper.writeValueAsString(searchParams);
		this.mockMvc.perform(post("/api/v1/flights/search")
			.contentType(MediaType.APPLICATION_JSON)
			.content(searchJSONString)
			.characterEncoding("utf-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));

	}

	@Test
	public void testSearchAcrossMultipleParams() throws Exception {
		SearchParams searchParams = new SearchParams();
		searchParams.setFlightType(BusinessFlightInfo.TYPE);
		searchParams.setArrivalCity("Humahuaca");
		searchParams.setDepartureCity("Comodoro Rivadavia");
		searchParams.setMaximumSearchResults(100);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchJSONString = objectMapper.writeValueAsString(searchParams);
		this.mockMvc.perform(post("/api/v1/flights/search")
			.contentType(MediaType.APPLICATION_JSON)
			.content(searchJSONString)
			.characterEncoding("utf-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));

	}

	@Test
	public void testSearchInvalidPage() throws Exception {
		SearchParams searchParams = new SearchParams();
		searchParams.setFlightType(CheapFlightInfo.TYPE);
		searchParams.setPage(44);
		searchParams.setMaximumSearchResults(100);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchJSONString = objectMapper.writeValueAsString(searchParams);
		this.mockMvc.perform(post("/api/v1/flights/search")
			.contentType(MediaType.APPLICATION_JSON)
			.content(searchJSONString)
			.characterEncoding("utf-8"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));

	}
}

