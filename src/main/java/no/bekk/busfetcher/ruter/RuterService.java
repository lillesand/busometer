package no.bekk.busfetcher.ruter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class RuterService {

    private final ObjectMapper mapper;
	private final AutoRetryHttpClient httpClient;

	public RuterService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PascalCaseStrategy());

		httpClient = new AutoRetryHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
	}

    public UpcomingDepartureToDowntown fetchRealtimeInformation() {
        try {
            HttpResponse response = httpClient.execute(new HttpGet("http://reis.trafikanten.no/reisrest/realtime/getrealtimedata/3010441"));
            InputStream inputStream = response.getEntity().getContent();

            //noinspection unchecked
            List<BusDepartureDto> departureDtos = (List<BusDepartureDto>) mapper.readValue(inputStream, new TypeReference<List<BusDepartureDto>>() { });

            return new UpcomingDepartureToDowntown(departureDtos);
        } catch (IOException e) {
            throw new RuterException(e);
        }
    }

}
