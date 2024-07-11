package com.rocketseat.planner.services.link;


import com.rocketseat.planner.domains.activity.Activity;

import com.rocketseat.planner.domains.link.Link;
import com.rocketseat.planner.domains.trip.Trip;
import com.rocketseat.planner.models.link.LinkData;
import com.rocketseat.planner.models.link.LinkRequestPayload;
import com.rocketseat.planner.models.link.LinkResponsePayload;
import com.rocketseat.planner.repository.link.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public LinkResponsePayload createLink(LinkRequestPayload payload, Trip trip) {
        Link link = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(link);

        return new LinkResponsePayload(link.getId());
    }

    public List<LinkData> getAllLinksFromId(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream().map(link ->
                new LinkData(link.getId(), link.getTitle(), link.getUrl())
        ).toList();
    }

}
