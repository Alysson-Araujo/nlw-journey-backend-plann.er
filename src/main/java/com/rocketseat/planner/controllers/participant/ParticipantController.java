package com.rocketseat.planner.controllers.participant;

import com.rocketseat.planner.domains.participant.Participant;
import com.rocketseat.planner.services.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;





    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipantPresence(@PathVariable UUID id, @RequestBody Participant payload) {
        Optional<Participant> participant = this.participantService.getParticipantById(id);

        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.getName());

            this.participantService.updateParticipant(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
