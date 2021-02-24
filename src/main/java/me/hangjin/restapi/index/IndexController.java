package me.hangjin.restapi.index;

import me.hangjin.restapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index() {
        RepresentationModel<?> index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events")); // html로 따지면 메뉴 추
        return index;
    }
}
