package me.dragonhee.demoinfleanrestapi.events;

import org.junit.jupiter.api.Test;

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

//        assertEquals(event.getName(),"hi");
        assertEquals(event.getName(), description);
    }

}