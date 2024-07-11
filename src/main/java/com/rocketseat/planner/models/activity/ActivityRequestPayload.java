package com.rocketseat.planner.models.activity;

import java.time.LocalDateTime;

public record ActivityRequestPayload(String title, String occurs_at) {
}
