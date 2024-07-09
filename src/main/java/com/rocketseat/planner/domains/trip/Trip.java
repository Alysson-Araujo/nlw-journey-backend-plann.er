package com.rocketseat.planner.domains.trip;


import com.rocketseat.planner.models.trip.TripRequestPayload;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
public class Trip {

    public Trip(TripRequestPayload payload){
        this.starts_at = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.ends_at = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.isConfirmed = false;
        this.owner_name = payload.owner_name();
        this.owner_email = payload.owner_email();
        this.destination = payload.destination();


    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime starts_at;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime ends_at;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String owner_name;

    @Column(name = "owner_email", nullable = false)
    private String owner_email;

    @Column(name = "destination", nullable = false)
    private String destination;
}
