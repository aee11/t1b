package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.*;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static FlightSearchEngine.Tables.*;

/**
 *
 */
public class FlightEngine {
    private Connection conn;
    private DSLContext create;
    private DateTimeFormatter dateFormatter;

    public FlightEngine(DatabaseConnection dbConn) {
        setupDbConnection(dbConn);
        dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    }

    private void setupDbConnection(DatabaseConnection dbConn) {
        conn = dbConn.getConnection();
        create = DSL.using(conn, SQLDialect.SQLITE);
    }

    /**
     * @param queryToSearchBy
     * @return
     */
    public List<FlightTrip> getResults(FlightQuery queryToSearchBy) {
        List<FlightTrip> flightTrips = getFlightTrips(queryToSearchBy);
        return flightTrips;
    }

    private List<FlightTrip> getFlightTrips(FlightQuery query) {
        if (query.getReturnTime() != null) {
            // Two-way flight query
            return getTwoWayFlightTrips(query);
        } else {
            // One-way flight query
            return getOneWayFlightTrips(query);
        }
    }

    private List<FlightTrip> getOneWayFlightTrips(FlightQuery query) {
        List<List<Flight>> flights = getFlightsBetween(query.getFromLocation(), query.getToLocation(),
                query.getDepartureTime(), query);
        List<FlightTrip> flightTrips = createFlightTrips(flights, null);
        return flightTrips;
    }

    private List<FlightTrip> getTwoWayFlightTrips(FlightQuery query) {

        List<List<Flight>> departureFlights = getFlightsBetween(query.getFromLocation(), query.getToLocation(),
                query.getDepartureTime(), query);
        List<List<Flight>> returnFlights = getFlightsBetween(query.getToLocation(), query.getFromLocation(),
                query.getReturnTime(), query);
        List<FlightTrip> flightTrips = createFlightTrips(departureFlights, returnFlights);
        return flightTrips;
    }

    private List<FlightTrip> createFlightTrips(List<List<Flight>> departureFlights, List<List<Flight>> returnFlights) {
        Boolean isOneWay = returnFlights == null;
        List<FlightTrip> flightTrips = new ArrayList<>();
        for (List<Flight> depFlight : departureFlights) {
            if (isOneWay) {
                flightTrips.add(new FlightTrip(depFlight));
            } else {
                for (List<Flight> retFlight : returnFlights) {
                    flightTrips.add(new FlightTrip(depFlight, retFlight));
                }
            }
        }
        return flightTrips;
    }

    private List<List<Flight>> getFlightsBetween(String fromLocation, String toLocation, LocalDate departureTime, FlightQuery query) {
        // From location A to B, with possibility of layover at location C
        // A --> C --> B or A --> B
        SelectQuery<Record> sQuery = create.select()
                .from(FLIGHTS)
                .where(FLIGHTS.DEPARTURELOCATION.equal(fromLocation)
                        .and(FLIGHTS.DEPARTURETIME.startsWith(departureTime.format(dateFormatter))))
                .or(FLIGHTS.ARRIVALLOCATION.equal(toLocation)
                        .and(FLIGHTS.ARRIVALTIME.startsWith(departureTime.format(dateFormatter))
                                .or(FLIGHTS.ARRIVALTIME.startsWith(departureTime.plusDays(1).format(dateFormatter)))))
                .getQuery();
        addOptionalConditions(sQuery, query);
        Result<Record> res = executeQuery(sQuery);
        List<Flight> flights = transformIntoFlights(res);
        return createFlightPaths(flights, fromLocation, toLocation);
    }

    private List<List<Flight>> createFlightPaths(List<Flight> flights, String from, String to) {
        List<List<Flight>> flightsWithConnectionFlights = new ArrayList<>();
        for (Flight flightA : flights) {
            if (flightA.getDepartureLocation().equals(from)) {
                for (Flight flightB : flights) {
                    if (flightB.getArrivalLocation().equals(to)) {
                        if (flightA.equals(flightB)) {
                            // Direct flight
                            List<Flight> flightList = new ArrayList<>();
                            flightList.add(flightA);
                            flightsWithConnectionFlights.add(flightList);
                        } else if (flightA.getArrivalLocation().equals(flightB.getDepartureLocation()) &&
                                flightA.getArrivalTime().isBefore(flightB.getDepartureTime())) {
                            // Connecting flight
                            List<Flight> flightList = new ArrayList<>();
                            flightList.add(flightA);
                            flightList.add(flightB);
                            flightsWithConnectionFlights.add(flightList);
                        }
                    }
                }
            }
        }
        return flightsWithConnectionFlights;
    }

    private List<Flight> transformIntoFlights(Result<Record> result) {
        List<Flight> flights = new ArrayList<>();
        DateTimeFormatter dbDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        for (Record res : result) {
            int flightNumber = (int) res.getValue(FLIGHTS.FLIGHTNUMBER);
            String sDepartureTime = (String) res.getValue(FLIGHTS.DEPARTURETIME);
            String sArrivalTime = (String) res.getValue(FLIGHTS.ARRIVALTIME);
            String departureLocation = (String) res.getValue(FLIGHTS.DEPARTURELOCATION);
            String arrivalLocation = (String) res.getValue(FLIGHTS.ARRIVALLOCATION);
            int price = (int) res.getValue(FLIGHTS.PRICE);
            String airline = (String) res.getValue(FLIGHTS.AIRLINE);
            int seatsAvailable = (int) res.getValue(FLIGHTS.SEATSAVAILABLE);
            LocalDateTime departureTime = LocalDateTime.parse(sDepartureTime, dbDateFormatter);
            LocalDateTime arrivalTime = LocalDateTime.parse(sArrivalTime, dbDateFormatter);
            Flight flight = new Flight(flightNumber, departureTime, arrivalTime, price,
                    departureLocation, arrivalLocation, airline, seatsAvailable);
            flights.add(flight);
        }
        return flights;
    }

    private void addOptionalConditions(SelectQuery<Record> query, FlightQuery flightQuery) {
        query.addConditions(FLIGHTS.SEATSAVAILABLE.greaterOrEqual(flightQuery.getSeatsRequired()));
    }

    /**
     * @param query
     * @return
     */
    private Result executeQuery(SelectQuery<Record> query) {
        return query.fetch();
    }
}
