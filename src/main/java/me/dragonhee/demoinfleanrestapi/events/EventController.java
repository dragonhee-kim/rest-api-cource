package me.dragonhee.demoinfleanrestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value ="/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventReposiroty eventReposiroty;

    public EventController(EventReposiroty eventReposiroty) {
        this.eventReposiroty = eventReposiroty;
    }

    @PostMapping
    public ResponseEntity createdEvent(@RequestBody Event event){


        Event newEvent = this.eventReposiroty.save(event);
        URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        System.out.println("tes "+ createdURI);


        return ResponseEntity.created(createdURI).body(event);
    }

}
