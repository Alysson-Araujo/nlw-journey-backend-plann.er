package com.rocketseat.planner.services.participant;

import com.rocketseat.planner.domains.participant.Participant;
import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.participant.ParticipantCreateResponse;
import com.rocketseat.planner.models.participant.ParticipantData;
import com.rocketseat.planner.repository.participant.ParticipantRepository;
import com.rocketseat.planner.repository.trip.TripRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ParticipantServiceTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setDestination("São Paulo, SP");
        trip.setStarts_at(LocalDateTime.parse("2024-07-24T21:51:53.734Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setEnds_at(LocalDateTime.parse("2024-08-24T21:51:53.734Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setOwner_name("Alysson Araújo");
        trip.setOwner_email("alysson@contato.com.br");
        trip.setIsConfirmed(false);
        trip = tripRepository.saveAndFlush(trip); // Certificando que o Trip seja persistido e atualizado

        // Assegure que o Trip foi salvo e tem um ID válido
        assertNotNull(trip.getId());
    }

    @Test
    void testRegisterParticipantsToEvent() {
        List<String> participantsToInvite = List.of("test1@example.com", "test2@example.com");

        participantService.registerParticipantsToEvent(participantsToInvite, trip);

        List<Participant> participants = participantRepository.findAll();
        assertEquals(2, participants.size());
    }

    @Test
    void testRegisterParticipantToEvent() {
        String email = "fulano@contato.com";

        ParticipantCreateResponse response = participantService.registerParticipantToEvent(email, trip);

        assertNotNull(response);
        Optional<Participant> participant = participantRepository.findById(response.id());
        assertTrue(participant.isPresent());
    }

    @Test
    void testGetParticipantById() {
        Participant participant = new Participant("fulano@contato.com", trip);
        participantRepository.save(participant);

        Optional<Participant> result = participantService.getParticipantById(participant.getId());

        assertTrue(result.isPresent());
        assertEquals(participant.getEmail(), result.get().getEmail());
    }

    @Test
    void testUpdateParticipant() {
        Participant participant = new Participant("fulano@contato.com", trip);
        participantRepository.save(participant);

        participant.setEmail("updated@example.com");
        participantService.updateParticipant(participant);

        Optional<Participant> updatedParticipant = participantRepository.findById(participant.getId());
        assertTrue(updatedParticipant.isPresent());
        assertEquals("updated@example.com", updatedParticipant.get().getEmail());
    }

    @Test
    void testGetAllParticipantsForEvent() {
        Participant participant1 = new Participant("fulano@example.com", trip);
        Participant participant2 = new Participant("cicrano@example.com", trip);
        participantRepository.save(participant1);
        participantRepository.save(participant2);

        List<ParticipantData> participants = participantService.getAllParticipantsFroeEvent(trip.getId());

        assertNotNull(participants);
        assertEquals(2, participants.size());
    }
}
