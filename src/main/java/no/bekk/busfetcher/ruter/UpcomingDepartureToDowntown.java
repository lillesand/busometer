package no.bekk.busfetcher.ruter;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.List;

public class UpcomingDepartureToDowntown {

    public static final String DOWNTOWN_DIRECTION_NAME = "2";

    private final DateTime expectedDepartureTime;

    public UpcomingDepartureToDowntown(List<BusDepartureDto> departureDtos) {
        this.expectedDepartureTime = findNextDepartureInRightDirection(departureDtos);
    }

    private DateTime findNextDepartureInRightDirection(List<BusDepartureDto> departureDtos) {
        for (BusDepartureDto departureDto : departureDtos) {
            if (departureDto.getDirectionName().equals(DOWNTOWN_DIRECTION_NAME)) {
                return departureDto.getExpectedDepartureTime();
            }
        }

        throw new RuterException("Couldn't find bus in right direction: " + DOWNTOWN_DIRECTION_NAME + ". Departures: " + departureDtos);
    }

    @Override
    public String toString() {
        return String.format("Next departure is in %s minutes, at %s", getWaitingTimeInMinutes(), expectedDepartureTime.toLocalTime());
    }

    public int getWaitingTimeInMinutes() {
        DateTime now = new DateTime();
        return Minutes.minutesBetween(now, this.expectedDepartureTime).getMinutes();
    }

}
