package me.hangjin.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getMaxPrice() < eventDto.getBasePrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "WrongValue", "basePrice is wrong");
            errors.rejectValue("maxPrice", "WrongValue", "maxPrice is wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
        ) {
            errors.rejectValue("endEventDateTime", "WrongValue", "endEventDateTime is wrong");
        }
    }
}
