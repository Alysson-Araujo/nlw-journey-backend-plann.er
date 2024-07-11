package com.rocketseat.planner.controllers.trip;

import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.activity.ActivityData;
import com.rocketseat.planner.models.activity.ActivityResponsePayload;
import com.rocketseat.planner.models.activity.ActivityRequestPayload;
import com.rocketseat.planner.models.link.LinkData;
import com.rocketseat.planner.models.link.LinkRequestPayload;
import com.rocketseat.planner.models.link.LinkResponsePayload;
import com.rocketseat.planner.models.participant.ParticipantData;
import com.rocketseat.planner.models.trip.TripRequestPayload;
import com.rocketseat.planner.services.activity.ActivityService;
import com.rocketseat.planner.services.link.LinkService;
import com.rocketseat.planner.services.participant.ParticipantService;
import com.rocketseat.planner.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private LinkService linkService;

    @Autowired
    private ActivityService activityService;

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

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponsePayload> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        if (trip.isPresent()) {

            Trip rawTrip = trip.get();

            ActivityResponsePayload activityResponsePayload = this.activityService.createActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponsePayload);

        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getActivities(@PathVariable UUID id) {
        List<ActivityData> activities = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activities);
    }


    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.participantService.getAllParticipantsFroeEvent(id);

        return ResponseEntity.ok(participants);
    }


    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponsePayload> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripService.getTripById(id);

        if (trip.isPresent()) {

            Trip rawTrip = trip.get();

            LinkResponsePayload linkResponsePayload = this.linkService.createLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponsePayload);

        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getLinks(@PathVariable UUID id) {
        List<LinkData> links = this.linkService.getAllLinksFromId(id);

        return ResponseEntity.ok(links);
    }
}
