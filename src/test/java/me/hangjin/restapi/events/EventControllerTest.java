package me.hangjin.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        Event event = Event.builder()
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
        //when


        //then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(status().isCreated());
    }


}