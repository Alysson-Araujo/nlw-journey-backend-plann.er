package com.rocketseat.planner.services.activity;

import com.rocketseat.planner.domains.activity.Activity;
import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.activity.ActivityData;
import com.rocketseat.planner.models.activity.ActivityResponsePayload;
import com.rocketseat.planner.models.activity.ActivityRequestPayload;
import com.rocketseat.planner.models.participant.ParticipantData;
import com.rocketseat.planner.repository.activity.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponsePayload createActivity(ActivityRequestPayload payload, Trip trip) {
        Activity activity = new Activity(payload.title(), payload.occurs_at(),trip);

        this.activityRepository.save(activity);

        return new ActivityResponsePayload(activity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity ->
                new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())
        ).toList();
    }
}
