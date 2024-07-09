package com.rocketseat.planner.models.trip;

import java.util.List;

public record TripRequestPayload(
        String starts_at,
        String ends_at,
        List<String> emails_to_invite,
        String owner_name,
        String owner_email,
        String destination
) {
}
