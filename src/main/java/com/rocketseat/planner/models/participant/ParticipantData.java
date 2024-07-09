package com.rocketseat.planner.models.participant;

import java.util.UUID;

public record ParticipantData(
        String name,
        String email, Boolean isConfirmed, UUID id) {

}
