package me.hangjin.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventsTest {

    @Test
    public void builder() throws Exception {
        //given
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        //when

        //then
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() throws Exception {
        //given 
        String name = "Event";
        String description = "Spring";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree() throws Exception {
        //given 
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isTrue();

        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();

        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();
    }


    @Test
    public void testOffline() throws Exception {
        //given 
        Event event = Event.builder()
                .location("네이버 D2 팩토리")
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

        event = Event.builder()
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isFalse();
    }


}