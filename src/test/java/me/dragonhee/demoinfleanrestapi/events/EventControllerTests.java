package me.dragonhee.demoinfleanrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonhee.demoinfleanrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest //web용 빈만 가져옴.
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventReposiroty eventReposiroty;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
//                .id(100)
                .name("Spring")
                .description("hi rest api welcome")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,02,01,23,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,02,02,23,50))
                .beginEventDateTime(LocalDateTime.of(2021,02,03,23,50))
                .endEventDateTime(LocalDateTime.of(2021,02,04,23,30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역~")
//                .free(true)
//                .offline(false)
                .build();

//        System.out.println("1============================");
//        Mockito.when(eventReposiroty.save(event)).thenReturn(event);
//        System.out.println("2============================");
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))

            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists("Location"))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE,"application/hal+json"))
            .andExpect(jsonPath("id").value(Matchers.not(100)))
            .andExpect(jsonPath("free").value(Matchers.not(true)))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))

        ;

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용하는 경우 에러 발생 테스트")
    public void createEvent_bad() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("hi rest api welcome")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,02,01,23,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,02,02,23,50))
                .beginEventDateTime(LocalDateTime.of(2021,02,03,23,50))
                .endEventDateTime(LocalDateTime.of(2021,02,04,23,30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역~")
                .free(true)
                .offline(false)
                .build();

//        System.out.println("1============================");
//        Mockito.when(eventReposiroty.save(event)).thenReturn(event);
//        System.out.println("2============================");
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isBadRequest())


        ;

    }

    @Test
    @TestDescription("입력 값이 비어 있는 경우 에러 발생 테스트")
    public void createEvent_Bad_Request_empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못 되어 있는 경우 에러 발생 테스트")
    public void createEvent_Bad_Request_empty_Input2() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("hi rest api welcome")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,02,03,23,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,02,03,23,50))
                .beginEventDateTime(LocalDateTime.of(2021,02,03,23,50))
                .endEventDateTime(LocalDateTime.of(2021,02,04,23,30))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역~")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}
