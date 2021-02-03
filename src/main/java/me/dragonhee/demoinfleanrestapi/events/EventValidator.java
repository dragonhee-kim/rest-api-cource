package me.dragonhee.demoinfleanrestapi.events;

//import org.modelmapper.internal.Errors;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice", "wrongValue","BasePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue","MaxPrice is wrong.");
         }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime","wronValue", " wrongValue time~");
        }
        // TODO beginEventDateTIme
        // TODO CloseEnrollmentDateTIme

    }
}
