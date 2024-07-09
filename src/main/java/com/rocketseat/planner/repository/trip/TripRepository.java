package com.rocketseat.planner.repository.trip;

import com.rocketseat.planner.domains.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

}