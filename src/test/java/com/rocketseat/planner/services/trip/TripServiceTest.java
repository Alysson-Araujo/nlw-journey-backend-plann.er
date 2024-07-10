package com.rocketseat.planner.services.trip;

import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.repository.trip.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = "com.rocketseat.planner")
@ActiveProfiles("test")
@Transactional
class TripServiceTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripService tripService;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setDestination("São Paulo, SP");
        trip.setStarts_at(LocalDateTime.parse("2024-07-24T21:51:53.734Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setEnds_at(LocalDateTime.parse("2024-08-24T21:51:53.734Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setOwner_name("Alysson Araújo");
        trip.setOwner_email("alysson@contato.com.br");
        trip.setIsConfirmed(false);
        trip = tripRepository.saveAndFlush(trip);
        assertNotNull(trip.getId());
    }

    @Test
    void testRegisterTrip() {
        Trip newTrip = new Trip();
        newTrip.setDestination("Rio de Janeiro, RJ");
        newTrip.setStarts_at(LocalDateTime.parse("2024-09-01T10:00:00", DateTimeFormatter.ISO_DATE_TIME));
        newTrip.setEnds_at(LocalDateTime.parse("2024-09-10T18:00:00", DateTimeFormatter.ISO_DATE_TIME));
        newTrip.setOwner_name("Carlos Silva");
        newTrip.setOwner_email("carlos@contato.com.br");
        newTrip.setIsConfirmed(true);

        tripService.registerTrip(newTrip);

        Optional<Trip> savedTrip = tripRepository.findById(newTrip.getId());
        assertTrue(savedTrip.isPresent());
        assertEquals("Rio de Janeiro, RJ", savedTrip.get().getDestination());
    }

    @Test
    void testGetTripById() {
        Optional<Trip> foundTrip = tripService.getTripById(trip.getId());

        assertTrue(foundTrip.isPresent());
        assertEquals(trip.getDestination(), foundTrip.get().getDestination());
    }

    @Test
    void testUpdateTrip() {
        trip.setDestination("Updated Destination");
        tripService.updateTrip(trip);

        Optional<Trip> updatedTrip = tripRepository.findById(trip.getId());
        assertTrue(updatedTrip.isPresent());
        assertEquals("Updated Destination", updatedTrip.get().getDestination());
    }
}
