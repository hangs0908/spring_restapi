package me.hangjin.restapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("testFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) throws Exception {
        //given 
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private static Stream<Arguments> testFree() {
        return Stream.of(
                Arguments.of(0,0,true),
                Arguments.of(0,100,false),
                Arguments.of(100,0,false),
                Arguments.of(100,200,false)
        );
    }


    @ParameterizedTest
    @MethodSource("testOffline")
    public void testOffline(String location, boolean isOffline) throws Exception {
          //given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private static Stream<Arguments> testOffline() {
        return Stream.of(
                Arguments.of("네이버 D2 팩토리", true),
                Arguments.of("", false),
                Arguments.of(null, false),
                Arguments.of(" ", false)
        );
    }




}