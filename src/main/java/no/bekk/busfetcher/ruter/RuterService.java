package no.bekk.busfetcher.ruter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AutoRetryHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RuterService {

    private final ObjectMapper mapper;

    public RuterService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PascalCaseStrategy());
    }

    public UpcomingDepartureToDowntown fetchRealtimeInformation() {
        try {
            HttpResponse response = new AutoRetryHttpClient().execute(new HttpGet("http://reis.trafikanten.no/reisrest/realtime/getrealtimedata/3010441"));
            InputStream inputStream = response.getEntity().getContent();

            //noinspection unchecked
            List<BusDepartureDto> departureDtos = (List<BusDepartureDto>) mapper.readValue(inputStream, new TypeReference<List<BusDepartureDto>>() { });

            return new UpcomingDepartureToDowntown(departureDtos);
        } catch (IOException e) {
            throw new RuterException(e);
        }
    }

}
