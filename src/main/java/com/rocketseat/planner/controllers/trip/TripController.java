package com.rocketseat.planner.controllers.trip;

import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.trip.TripRequestPayload;
import com.rocketseat.planner.services.participant.ParticipantService;
import com.rocketseat.planner.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<String> createTrip(@RequestBody
                                             TripRequestPayload payload) {
        Trip trip = new Trip(payload);

        this.tripService.registerTrip(trip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), trip.getId());

        return ResponseEntity.ok("O Trip foi criado com sucesso!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        return trip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
