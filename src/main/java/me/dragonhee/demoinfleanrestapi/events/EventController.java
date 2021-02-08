package me.dragonhee.demoinfleanrestapi.events;

import me.dragonhee.demoinfleanrestapi.common.ErrorsResource;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.Errors;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto,Event.class);
        event.update();
        Event newEvent = this.eventReposiroty.save(event);
        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdURI = webMvcLinkBuilder.toUri();


        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(webMvcLinkBuilder.withSelfRel()); -> EventResource에서 넣음.
        eventResource.add(webMvcLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events-created").withRel("profile"));

        return ResponseEntity.created(createdURI).body(eventResource);
    }


    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> page = this.eventReposiroty.findAll(pageable);
        //assemboler를 통해 page를 resource로 만들수 있따.
        var pagedResources = assembler.toModel(page, e -> new EventResource(e));
        //아래 코드와 테스트 코드쪽을  통해 generated-snippets 밑에 디렉토리가 하나 생긴다.
        //URL은 profile
        pagedResources.add(new Link("/docs/index.html#resources-events-query").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventReposiroty.findById(id);

        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));


        return ResponseEntity.ok(eventResource);

    }

    public ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
