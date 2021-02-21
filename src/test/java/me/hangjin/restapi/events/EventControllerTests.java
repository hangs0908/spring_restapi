package me.hangjin.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc //mock web 환경이 제공되. 웹 서버를 띄우지 않고 테스트를 가능하게 한
@SpringBootTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 가짜 요청을 만들어서 dispacherservlet으로 보내고 거기에 따른 응답을 검증할 수 있다. 웹서버를 띄우지 않ㄴ는다.

    @Autowired
    ObjectMapper objectMapper; // 객체를 -> JSON 으로 바꾸기 위함, 스프링부트가 자동적으로 objectmapper를 빈으로 등록해서 사용가능하다.

//    @MockBean
//    EventRepository eventRepository; // mcok 객체이기 때문에 nullpoint exception 발생 그래서 값을 정해줘야 한다.

    @Test
    public void createEvent() throws Exception {
        //given
        Event event = Event.builder()
                .id(100) //무시됨
                .name("Spring")
                .description("REST API development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitEnrollment(100)
                .location("강남역 D2 스타터 팩토리")
                .free(true) // 무시됨
                .offline(false) //무시됨
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        //when
//        event.setId(10);
//        when(eventRepository.save(event)).thenReturn(event);

        //then
        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON) // 요청의 contentType은 JSON 이다.
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))) // 요청 헤더에 클라이언트가 어떤 응답을 받고 싶는지 적는 accept 헤더에 적는것이다. 그래서 HAL_JSON 이라는 미디어 타입을 받고 싶다는 의미
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)));
    }


}
