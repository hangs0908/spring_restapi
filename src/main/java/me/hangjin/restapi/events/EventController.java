package me.hangjin.restapi.events;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event newEvent = eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
        // linkto(methodOn eventController의 클래스에 있는 메소드 createEvent() 에서 uri 를 불러오는 것이다.
        // 그래서 toUri() 로 새로운 uri를 만들어 내고. -> /api/events/{id}
        // 이를  응답 메세지에 보내게 된다.
    }
}