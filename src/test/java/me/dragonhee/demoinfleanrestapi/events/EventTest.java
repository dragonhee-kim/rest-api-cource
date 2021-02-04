package me.dragonhee.demoinfleanrestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("kim yong hee")
                .description("REST API")
                .build();
        assertNotNull(event);
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        event.setName("kim");
        String description = "hi";
        event.setDescription(description);

//        assertEquals(event.getName(), description);
    }

    @Test
    public void testFree(){
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        assertEquals(event.isFree(),true);

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        assertEquals(event.isFree(),false);

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(1000)
                .build();
        // When
        event.update();
        // Then
        assertEquals(event.isFree(),false);

    }

    @Test
    public void testOffline(){
        // Given
        Event event = Event.builder()
                .location("강남역 네이버 D2")
                .build();
        // When
        event.update();
        // Then
        assertEquals(event.isOffline(),true);

        // Given
        event = Event.builder()
                .build();
        // When
        event.update();
        // Then
        assertEquals(event.isOffline(),false);

    }

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();

        // then
        assertEquals(event.isOffline(),isFree);

    }

    @ParameterizedTest
    @MethodSource("paramsForTestOffline")
    void testOffline(String location, boolean isOffline) {
        // given
        Event event = Event.builder()
                .location(location)
                .build();

        // when
        event.update();

        // then
        assertEquals(event.isOffline(),isOffline);

    }

    private static Stream<Arguments> paramsForTestFree() { // argument source method
        return Stream.of(
                Arguments.of(0,0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false),
                Arguments.of(100, 200, false)
        );
    }

    private static Stream<Arguments> paramsForTestOffline() { // argument source method
        return Stream.of(
                Arguments.of("강남", true),
                Arguments.of(null, false),
                Arguments.of("        ", false)
        );
    }

}