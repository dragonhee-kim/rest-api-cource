package me.dragonhee.demoinfleanrestapi.common;

import me.dragonhee.demoinfleanrestapi.events.Event;
import me.dragonhee.demoinfleanrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Event> {
    public ErrorsResource (Errors content, Link... links){
        super((Event) content,links);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));

    }
}
