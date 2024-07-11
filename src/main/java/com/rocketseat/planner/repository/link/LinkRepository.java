package com.rocketseat.planner.repository.link;

import com.rocketseat.planner.domains.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {

        List<Link> findByTripId(UUID tripId);
}
