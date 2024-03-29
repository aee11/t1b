package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static FlightSearchEngine.Tables.FLIGHTS;

/**
 *
 */
public class FlightEngine {
    private Connection conn;
    private DSLContext create;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dbDateFormatter;

    public FlightEngine(DatabaseConnection dbConn) {
        setupDbConnection(dbConn);
        dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        dbDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
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
        List<FlightTrip> flightTrips;
        if (query.getReturnTime() != null) {
            // Two-way flight query
            flightTrips = getTwoWayFlightTrips(query);
        } else {
            // One-way flight query
            flightTrips = getOneWayFlightTrips(query);
        }
        if (query.getNightFlightsOnly()) {
            removeNightFlights(flightTrips);
        }
        if (query.getConnectionTimeMin() != 0) { // Connection time declared
            filterByConnectionTime(flightTrips, query);
        }
        return flightTrips;
    }

    private void filterByConnectionTime(List<FlightTrip> flightTrips, FlightQuery query) {
        List<FlightTrip> flightTripsToRemove = new ArrayList<FlightTrip>(); // Can't delete flightTrip in for-loop (concurrency error)
        Duration allowedMinDuration = Duration.ofMinutes(query.getConnectionTimeMin());
        Duration allowedMaxDuration = Duration.ofMinutes(query.getConnectionTimeMax());
        for (FlightTrip flightTrip : flightTrips) {
            List<Flight> departureFlights = flightTrip.getDepartureFlights();
            List<Flight> returnFlights = flightTrip.getReturnFlights();
            if (departureFlights.size() == 2) { // Layover flight?
                LocalDateTime layoverArrival = departureFlights.get(0).getArrivalTime();
                LocalDateTime layoverDeparture = departureFlights.get(1).getDepartureTime();
                Duration connectionDuration = Duration.between(layoverArrival, layoverDeparture);
                if (!(   connectionDuration.getSeconds() >= allowedMinDuration.getSeconds()
                      && connectionDuration.getSeconds() <= allowedMaxDuration.getSeconds())) {
                    flightTripsToRemove.add(flightTrip);
                    continue;
                }
            }
            if (returnFlights != null && returnFlights.size() == 2) {
                LocalDateTime layoverArrival = returnFlights.get(0).getArrivalTime();
                LocalDateTime layoverDeparture = returnFlights.get(1).getDepartureTime();
                Duration connectionDuration = Duration.between(layoverArrival, layoverDeparture);
                if (   connectionDuration.getSeconds() >= allowedMinDuration.getSeconds()
                    && connectionDuration.getSeconds() <= allowedMaxDuration.getSeconds()) {
                    flightTripsToRemove.add(flightTrip);
                }
            }
        }
        for (FlightTrip badTrip : flightTripsToRemove) {
            flightTrips.remove(badTrip);
        }
    }

    private void removeNightFlights(List<FlightTrip> flightTrips) {
        List<FlightTrip> flightTripsToRemove = new ArrayList<FlightTrip>();
        for (FlightTrip flightTrip : flightTrips) {
            for (Flight depFlight : flightTrip.getDepartureFlights()) {
                LocalDateTime departureTime = depFlight.getDepartureTime();
                LocalDateTime departureDateMidnight = departureTime.truncatedTo(ChronoUnit.DAYS);

                // If the flight is between 00:00 and 07:00 on the date of the flight
                // OR the flight is after   22:00
                if ((departureTime.isAfter(departureDateMidnight) &&
                        departureTime.isBefore(departureDateMidnight.plusHours(7))) ||
                        departureTime.isAfter(departureDateMidnight.plusHours(22))) {
                    flightTripsToRemove.add(flightTrip);
                    break;
                }
            }
        }
        for (FlightTrip badTrip : flightTripsToRemove) {
            flightTrips.remove(badTrip);
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
        List<FlightTrip> flightTrips = new ArrayList<FlightTrip>();
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
        List<List<Flight>> flightsWithConnectionFlights = new ArrayList<List<Flight>>();
        for (Flight flightA : flights) {
            if (flightA.getDepartureLocation().equals(from)) {
                for (Flight flightB : flights) {
                    if (flightB.getArrivalLocation().equals(to)) {
                        if (flightA.equals(flightB)) {
                            // Direct flight
                            List<Flight> flightList = new ArrayList<Flight>();
                            flightList.add(flightA);
                            flightsWithConnectionFlights.add(flightList);
                        } else if (flightA.getArrivalLocation().equals(flightB.getDepartureLocation()) &&
                                flightA.getArrivalTime().isBefore(flightB.getDepartureTime())) {
                            // Connecting flight
                            List<Flight> flightList = new ArrayList<Flight>();
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
        List<Flight> flights = new ArrayList<Flight>();

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
     * @param query Query to fetch results from.
     * @return Rows from database according to query.
     */
    private Result executeQuery(SelectQuery<Record> query) {
        return query.fetch();
    }

    /**
     * @param period A time period where special offers will be searched for.
     * @param numberOfOffers Maximum number of special offers you want to receive. Valid range is between 1 and 100.
     * @return List of flight trips that are special offers.
     */
    public List<FlightTrip> getSpecialOffers(Period period, int numberOfOffers) {
        if (numberOfOffers < 0 || numberOfOffers > 100) {
            throw new IllegalArgumentException(numberOfOffers + " is not in range 1..100");
        }
        LocalDateTime today = LocalDateTime.now();

        // Add one day to upperDate so the results includes the upperDate
        LocalDateTime upperDate = LocalDateTime.now().plus(period).plus(Period.ofDays(1));
        SelectQuery<Record> query = create.select()
                                          .from(FLIGHTS)
                                          .where(FLIGHTS.DEPARTURETIME.between(today.format(dateFormatter))
                                                  .and(upperDate.format(dateFormatter)))
                                          .orderBy(FLIGHTS.PRICE)
                                          .limit(numberOfOffers)
                                          .getQuery();
        Result<Record> results = query.fetch();

        List<FlightTrip> offers = new ArrayList<FlightTrip>();

        List<Flight> flights = transformIntoFlights(results);
        for (Flight depFlight : flights) {
            List<Flight> departureFlights = new ArrayList<Flight>();
            departureFlights.add(depFlight);
            FlightTrip flightTrip = new FlightTrip(departureFlights);
            offers.add(flightTrip);
        }
        return offers;
    }
}
