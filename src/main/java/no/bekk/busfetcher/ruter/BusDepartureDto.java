package no.bekk.busfetcher.ruter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

@JsonIgnoreProperties({"Extensions", "FramedVehicleJourneyRef"})
class BusDepartureDto {

    /**
     * retarded date strings
     */
    private String ExpectedDepartureTime;

    private String DestinationName, DirectionName;

    public DateTime getExpectedDepartureTime() {
        return dateTime(ExpectedDepartureTime);
    }

    public void setExpectedDepartureTime(String expectedDepartureTime) {
        ExpectedDepartureTime = expectedDepartureTime;
    }

    public String getDestinationName() {
        return DestinationName;
    }

    public void setDestinationName(String destinationName) {
        DestinationName = destinationName;
    }

    public String getDirectionName() {
        return DirectionName;
    }

    public void setDirectionName(String directionName) {
        DirectionName = directionName;
    }

    private DateTime dateTime(String aimedDepartureTime) {
        String millisString = StringUtils.substringBetween(aimedDepartureTime, "(", "+");
        long millisLong = Long.parseLong(millisString);
        return new DateTime(millisLong);
    }

    @Override
    public String toString() {
        return this.getDestinationName() + "(" + this.getDirectionName() + "): " + this.getExpectedDepartureTime().toString("HH:mm");
    }
}


