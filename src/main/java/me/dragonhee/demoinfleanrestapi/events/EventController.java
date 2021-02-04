package me.dragonhee.demoinfleanrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.validation.Errors;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value ="/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventReposiroty eventReposiroty;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventReposiroty eventReposiroty, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventReposiroty = eventReposiroty;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }
    //Errors 는 beanserialize 준수 안함
    //그래서 바로 body(errors) 사용못함. json으로 바로 변환이 안됨
    //어떻게 해결하지?

    @PostMapping
    public ResponseEntity createdEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto,Event.class);
        event.update();
        Event newEvent = this.eventReposiroty.save(event);
        URI createdURI = linkTo(EventController.class).slash(newEvent.getId()).toUri();


        return ResponseEntity.created(createdURI).body(event);
    }

}
