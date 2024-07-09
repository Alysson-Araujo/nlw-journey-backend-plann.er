package com.rocketseat.planner.services.participant;

import com.rocketseat.planner.domains.participant.Participant;
import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.participant.ParticipantCreateResponse;
import com.rocketseat.planner.models.participant.ParticipantData;
import com.rocketseat.planner.repository.participant.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List <Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);

    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);
        return new ParticipantCreateResponse(participant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {

    }

    public void triggerConfirmationEmailToParticipant(UUID tripId) {

    }

    public Optional<Participant> getParticipantById(UUID id) {
        return participantRepository.findById(id);
    }

    public void updateParticipant(Participant participant) {
        participantRepository.save(participant);
    }

    public List<ParticipantData> getAllParticipantsFroeEvent(UUID tripId) {
        return participantRepository.findByTripId(tripId).stream().map(participant ->
            new ParticipantData(participant.getName(), participant.getEmail(), participant.getIsConfirmed(), participant.getId())
        ).toList();
    }

}
