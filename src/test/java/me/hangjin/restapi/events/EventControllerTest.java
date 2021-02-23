package me.hangjin.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

    @Autowired
    MockMvc mockMvc; //mocking이 되어있는 dispacherServlet

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        //given
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest api with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,1,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,1,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,1,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,1,21))
                .basePrice(100)
                .maxPrice(200)
                .limitEnrollment(100)
                .location("강남역 2번 출구 카")
                .build();


        //then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
        ;
    }


    @Test
    public void createEvent_Bad_Request() throws Exception {
        //given
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest api with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,1,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,1,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,1,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,1,21))
                .basePrice(100)
                .maxPrice(200)
                .limitEnrollment(100)
                .location("강남역 2번 출구 카")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        //then
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // spring.deserialization.fail-on~ : true / json -> 객체일때 알려지지 않는 프로퍼티가 올때 fail 이 true
        ;
    }


    @Test
    @DisplayName("정해진 입력값으로 잘 들어오지만, 비어있는 입력값이 들어올 경우 Bad request")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        //given 
        EventDto eventDto = EventDto.builder().build();

        //when 

        //then
        this.mockMvc.perform(post("/api/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정해진 입력값으로 잘 들어오지만, 잘못된 입력값이 들어올 경우 Bad request")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        //given
        EventDto eventDto = EventDto.builder().build();

        //when

        //then
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

}