package me.dragonhee.demoinfleanrestapi.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReposiroty extends JpaRepository<Event, Integer> {

}
