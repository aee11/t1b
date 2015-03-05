package FlightSearchEngine;

import java.util.Date;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DSL.*;

/**
 * Created by Alexander on 18.2.2015.
 */
public class FlightQuery {
    private String flightQuery;
//    DSLContext create = DSL.using(configuration);

    public FlightQuery setDepartureTime(Date time) {
//        String sql = create.select(field("BOOK.TITLE"), field("AUTHOR.FIRST_NAME"), field("AUTHOR.LAST_NAME"))
//                .from(table("BOOK"))
//                .join(table("AUTHOR"))
//                .on(field("BOOK.AUTHOR_ID").equal(field("AUTHOR.ID")))
//                .where(field("BOOK.PUBLISHED_IN").equal(1948))
//                .getSQL();
        return this;
    }

    public FlightQuery setReturnTime(Date time) {
        return this;
    }

    public FlightQuery setFrom(String location) {
        return this;
    }

    public FlightQuery setTo(String location) {
        return this;
    }

    public FlightQuery sortBy(String field) // default: by price
    {
        return this;
    }

    public FlightQuery allowLayover(Boolean layoverAllowed) {
        return this;
    }

    public FlightQuery setLayoverTime(int minTime, int maxTime) {
        return this;
    }

    public FlightQuery setOnlyNightFlights(Boolean nightFlightsOnly) {
        return this;
    }

    public FlightQuery setSeatsRequired(int numSeats) {
        return this;
    }

    public FlightQuery excludeAirlines(String[] airlines) {
        return this;
    }

    public FlightQuery includeAirlines(String[] airlines) {
        return this;
    }

    public FlightTrip[] fetch() {
        return new FlightTrip[0];
    }
}
