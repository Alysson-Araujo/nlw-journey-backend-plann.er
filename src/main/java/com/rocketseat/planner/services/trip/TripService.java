package com.rocketseat.planner.services.trip;


import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.repository.trip.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public void registerTrip(Trip trip) {
        tripRepository.save(trip);
    }

    public Optional<Trip> getTripById(UUID id) {
        return tripRepository.findById(id);
    }

    public void updateTrip(Trip trip) {
        tripRepository.save(trip);
    }
}
