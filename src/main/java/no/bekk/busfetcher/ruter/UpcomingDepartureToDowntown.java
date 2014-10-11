package no.bekk.busfetcher.ruter;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.joda.time.DateTime.now;

public class UpcomingDepartureToDowntown {

    public static final String DOWNTOWN_DIRECTION_NAME = "2";
    public static final String BUS_LINE = "30";

    private final DateTime expectedDepartureTime;

    public UpcomingDepartureToDowntown(List<BusDepartureDto> departureDtos) {
        this.expectedDepartureTime = findNextDepartureInRightDirection(departureDtos);
    }

    private DateTime findNextDepartureInRightDirection(List<BusDepartureDto> departureDtos) {
        Optional<BusDepartureDto> firstDepartingBus = departureDtos.stream()
                .filter(busDepartureDto -> DOWNTOWN_DIRECTION_NAME.equals(busDepartureDto.getDirectionName()))
                .filter(busDepartureDto -> BUS_LINE.equals(busDepartureDto.getPublishedLineName()))
                // Minimum expected departing time is the closest bus to leaving
                .min(Comparator.comparing(BusDepartureDto::getExpectedDepartureTime));

        if (!firstDepartingBus.isPresent()) {
            throw new RuterException("Couldn't find bus in right direction: " + DOWNTOWN_DIRECTION_NAME + ". Departures: " + departureDtos);
        }
        else {
            return firstDepartingBus.get().getExpectedDepartureTime();
        }
    }

    @Override
    public String toString() {
        return String.format("Next departure is in %s minutes, at %s", getWaitingTimeInMinutes(), expectedDepartureTime.toLocalTime());
    }

    public int getWaitingTimeInMinutes() {
        return Minutes.minutesBetween(now(), this.expectedDepartureTime).getMinutes();
    }

}
