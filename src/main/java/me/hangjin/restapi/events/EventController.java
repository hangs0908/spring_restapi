package me.hangjin.restapi.events;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EventController {


    @PostMapping("/api/events")
    public ResponseEntity createEvent(@RequestBody Event event) {
        URI newUri = linkTo(methodOn(EventController.class).createEvent(event)).slash(event.getId()).toUri();
        return ResponseEntity.created(newUri).build();
    }


}
