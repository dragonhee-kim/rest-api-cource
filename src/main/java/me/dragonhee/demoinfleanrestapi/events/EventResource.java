package me.dragonhee.demoinfleanrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//public class EventResource extends RepresentationModel {
//
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event){
//        this.event = event;
//    }
//    public Event getEvent(){
//        return event;
//    }
//
//}

//위에서는 JsonWrapped를 사용
//밑에는 EntityModel을 사용.
//아래 처럼 사용하면 자동으로 unwrap이 됨.
public class EventResource extends EntityModel<Event> {


    public EventResource(Event event, Link... links){
        super(event,links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }


}