package com.rocketseat.planner.repository.participant;


import com.rocketseat.planner.domains.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {


    List<Participant> findByTripId(UUID tripId);
}
