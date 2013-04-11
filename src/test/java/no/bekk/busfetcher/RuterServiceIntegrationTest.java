package no.bekk.busfetcher;

import no.bekk.busfetcher.ruter.RuterService;
import no.bekk.busfetcher.ruter.UpcomingDepartureToDowntown;
import org.junit.Test;

public class RuterServiceIntegrationTest {

    @Test
    public void test() {
        UpcomingDepartureToDowntown upcomingDepartureToDowntown = new RuterService().fetchRealtimeInformation();
        System.out.println(upcomingDepartureToDowntown);
    }

}
