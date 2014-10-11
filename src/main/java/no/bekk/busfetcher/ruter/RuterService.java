package no.bekk.busfetcher.ruter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import no.bekk.busfetcher.util.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RuterService {

    private final ObjectMapper mapper;
	private final AutoRetryHttpClient httpClient;

	public RuterService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PascalCaseStrategy());

		httpClient = new AutoRetryHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
	}

    public UpcomingDepartureToDowntown fetchRealtimeInformation() {

        InputStream inputStream = null;
        try {
            Logger.log("Calling Ruter");
            HttpResponse response = httpClient.execute(new HttpGet("http://reis.trafikanten.no/reisrest/realtime/getrealtimedata/3010441"));
            inputStream = response.getEntity().getContent();

            //noinspection unchecked
            List<BusDepartureDto> departureDtos = mapper.readValue(inputStream, new TypeReference<List<BusDepartureDto>>() { });
            Logger.log("Read response");

            return new UpcomingDepartureToDowntown(departureDtos);
        }
        catch (IOException e) {
            throw new RuterException(e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    Logger.log("Couldn't close stream because " + e.getClass() + " " + e.getMessage());
                }
            }
        }
    }

}
