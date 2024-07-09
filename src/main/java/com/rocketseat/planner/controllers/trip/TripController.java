package com.rocketseat.planner.controllers.trip;

import com.rocketseat.planner.domains.participant.Participant;
import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.participant.ParticipantCreateResponse;
import com.rocketseat.planner.models.participant.ParticipantData;
import com.rocketseat.planner.models.participant.ParticipantRequestPayload;
import com.rocketseat.planner.models.trip.TripRequestPayload;
import com.rocketseat.planner.services.participant.ParticipantService;
import com.rocketseat.planner.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), trip);

        return ResponseEntity.ok("O Trip foi criado com sucesso!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        return trip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setStarts_at(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEnds_at(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            tripService.updateTrip(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            tripService.updateTrip(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invites")
    public ResponseEntity<String> inviteParticipants(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        if (trip.isPresent()) {

            Trip rawTrip = trip.get();

            List<String> participantToInvite = new ArrayList<>();
            participantToInvite.add(payload.email());

            ParticipantCreateResponse participantCreateResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            this.participantService.registerParticipantsToEvent(participantToInvite, rawTrip);

            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(rawTrip.getId());


            return ResponseEntity.ok("Os participantes foram convidados com sucesso!");
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.participantService.getAllParticipantsFroeEvent(id);

        return ResponseEntity.ok(participants);
    }

}
