package me.hangjin.restapi.events;

import me.hangjin.restapi.accounts.Account;
import me.hangjin.restapi.accounts.AccountRepository;
import me.hangjin.restapi.accounts.AccountRole;
import me.hangjin.restapi.accounts.AccountService;
import me.hangjin.restapi.common.BaseControllerTest;
import me.hangjin.restapi.configs.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    EventRepository eventRepository;
//    @MockBean
//    EventRepository eventRepository; // mcok 객체이기 때문에 nullpoint exception 발생 그래서 값을 정해줘야 한다.

    @Autowired
    AppProperties appProperties;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("정상적으로 이벤틀르 생성하는 테스트")
    public void createEvent() throws Exception {
        //given
        EventDto event = EventDto.builder()
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
                .build();

        //when
//        event.setId(10);
//        when(eventRepository.save(event)).thenReturn(event);

        //then
        mockMvc.perform(post("/api/events/")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON) // 요청의 contentType은 JSON 이다.
                    .accept(MediaTypes.HAL_JSON+";charset=utf-8")
                    .content(objectMapper.writeValueAsString(event))) // 요청 헤더에 클라이언트가 어떤 응답을 받고 싶는지 적는 accept 헤더에 적는것이다. 그래서 HAL_JSON 이라는 미디어 타입을 받고 싶다는 의미
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE+";charset=utf-8"))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to update an existing event")

                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enroll time"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enroll time"),
                                fieldWithPath("beginEventDateTime").description("begin event time"),
                                fieldWithPath("endEventDateTime").description("end event time"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        /**
                         * relaxedResponseFields 를 쓰면 모든 필드를 기술할 필요가 없다.
                         * 하지만 모든 필드를 기술하지 않으므로 정확한 문서를 만들지 못한다.
                         * responseFields를 쓰면 모든 필드를 기술해야 한다.
                         */
                        relaxedResponseFields(
                                fieldWithPath("id").description("Identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enroll time"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enroll time"),
                                fieldWithPath("beginEventDateTime").description("begin event time"),
                                fieldWithPath("endEventDateTime").description("end event time"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                        ))
        ;
    }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
//        String clientId = "myApp"; // 우리가 만들고 있는 Application ID
//        String clientSecret = "pass"; // 그에 대한 비밀번호
//
//        String username = "hangjin@email.com";
//        String password = "hangjin";

        Account hangjin = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(hangjin);
        //when

        //then
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret())) //clientId와 ClientSecret을 가지고Basic Oauth라는 헤더를 만든것
                .param("username", appProperties.getUserUsername()) // 파라미터로 grant type , username, password를 줘야함
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password")
        );
        String responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }


    @Test
    @DisplayName("입력 받을 수 없는 값을 사용하는 경우에 에러가 발생하는 테스")
    public void createEvent_Bad_Request() throws Exception {
        //given
        Event event = Event.builder()
                .id(100)
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON) // 요청의 contentType은 JSON 이다.
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))) // 요청 헤더에 클라이언트가 어떤 응답을 받고 싶는지 적는 accept 헤더에 적는것이다. 그래서 HAL_JSON 이라는 미디어 타입을 받고 싶다는 의미
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        //given 
        EventDto eventDto = EventDto.builder().build();
        //when
        this.mockMvc.perform(post("/api/events")
                            .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest());

        //then 
    }


    @Test
    @DisplayName("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception { // 입력값이 이상한 경우에는 bad request 나와야
        //given
        EventDto eventDto = EventDto.builder()
                            .name("Spring")
                            .description("REST API development with Spring")
                            .beginEnrollmentDateTime(LocalDateTime.of(2018,11,26,14,21))
                            .closeEnrollmentDateTime(LocalDateTime.of(2018,11,25,14,21))
                            .beginEventDateTime(LocalDateTime.of(2018,11,24,14,21))
                            .endEventDateTime(LocalDateTime.of(2018,11,23,14,21))
                            .basePrice(10000)
                            .maxPrice(200)
                            .limitEnrollment(100)
                            .location("강남역 D2 스타터 팩토리")
                            .build();
        //when
        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                //.andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
                //.andExpect(jsonPath("$[0].rejectedValue").exists());

        //then
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조획하기.")
    public void queryEvents() throws Exception {
        //given
        IntStream.range(0,30).forEach(this::generateEvent);
        
        //when
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort","name,DESC")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));


        //then 
    }


    @Test
    @DisplayName("기존의 이벤트 하나 조회하")
    public void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when

        //then
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @DisplayName("없는 이벤트를 조희시 404 응답받기")
    public void getEvent404() throws Exception {
        //given

        //when


        //then
        this.mockMvc.perform(get("/api/events/12314"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //when
        eventDto.setName("Updated Event");

        //then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Updated Event"))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }


    @Test
    @DisplayName("입력값이 비어있 경우에 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception {
        //given
        Event event = generateEvent(200);

        //when
        EventDto eventDto = new EventDto();

        //then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        //given
        Event event = generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        //when
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(2000);

        //then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //when

        //then
        this.mockMvc.perform(put("/api/events/12312")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    private Event generateEvent(int index) {
        Event event =Event.builder()
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
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }

}
