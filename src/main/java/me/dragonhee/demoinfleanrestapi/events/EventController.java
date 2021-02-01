package me.dragonhee.demoinfleanrestapi.events;

import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    public EventController(EventReposiroty eventReposiroty, ModelMapper modelMapper) {
        this.eventReposiroty = eventReposiroty;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createdEvent(@RequestBody EventDto eventDto){
//        System.out.println("1111");
        Event event = modelMapper.map(eventDto,Event.class);
//        System.out.println("22222");
        Event newEvent = this.eventReposiroty.save(event);
        URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();
//        System.out.println("tes "+ createdURI);


        return ResponseEntity.created(createdURI).body(event);
    }

}
