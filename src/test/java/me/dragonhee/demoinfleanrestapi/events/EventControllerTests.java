package me.dragonhee.demoinfleanrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonhee.demoinfleanrestapi.accounts.Account;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRepository;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRole;
import me.dragonhee.demoinfleanrestapi.accounts.AccountService;
import me.dragonhee.demoinfleanrestapi.common.AppProperties;
import me.dragonhee.demoinfleanrestapi.common.BaseTestController;
import me.dragonhee.demoinfleanrestapi.common.RestDocsConfiguration;
import me.dragonhee.demoinfleanrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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


public class EventControllerTests extends BaseTestController {



    @Autowired
    EventReposiroty eventReposiroty;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void setUp(){
        this.eventReposiroty.deleteAll();
        this.accountRepository.deleteAll();
    }

//    @MockBean
//    EventReposiroty eventReposiroty;
//
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))

            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists("Location"))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE,"application/hal+json"))
            .andExpect(jsonPath("id").value(Matchers.not(100)))
            .andExpect(jsonPath("free").value(false))
            .andExpect(jsonPath("offline").value(true))

            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
//            .andExpect(jsonPath("_links.self").exists())
//            .andExpect(jsonPath("_links.query-events").exists())
//            .andExpect(jsonPath("_links.update-event").exists())
            .andDo(document("create-event",
                    links(
                            linkWithRel("self").description("link to self"),
                            linkWithRel("query-events").description("link to query events"),
                            linkWithRel("update-event").description("link to update an existing events"),
                            linkWithRel("profile").description("profile")

                            ),
                    requestHeaders(
                            headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("location type header")
                    ),
                    requestFields(
                            fieldWithPath("name").description("Name of new event"),
                            fieldWithPath("description").description("description of new event"),
                            fieldWithPath("beginEnrollmentDateTime").description("data time of beging of new event"),
                            fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                            fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                            fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                            fieldWithPath("location").description("location of new event"),
                            fieldWithPath("basePrice").description("basePrice of new event"),
                            fieldWithPath("maxPrice").description("maxPrice of new event"),
                            fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")

                    ),
                    responseHeaders(
                            headerWithName(HttpHeaders.LOCATION).description("location header"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                    ),
                    relaxedResponseFields(
//                    relaxedResponseFields(

                            fieldWithPath("id").description("id of new event"),
                            fieldWithPath("name").description("Name of new event"),
                            fieldWithPath("description").description("description of new event"),
                            fieldWithPath("beginEnrollmentDateTime").description("data time of beging of new event"),
                            fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                            fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                            fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                            fieldWithPath("location").description("location of new event"),
                            fieldWithPath("basePrice").description("basePrice of new event"),
                            fieldWithPath("maxPrice").description("maxPrice of new event"),
                            fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                            fieldWithPath("free").description("free of new event"),
                            fieldWithPath("offline").description("offline of new event"),
                            fieldWithPath("eventStatus").description("eventStatus of new event"),

                            //optional fields
                            fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("my href").optional(),
                            fieldWithPath("_links.query-events.href").type(JsonFieldType.STRING).description("my href").optional(),
                            fieldWithPath("_links.update-event.href").type(JsonFieldType.STRING).description("my href").optional(),
                            fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("profile").optional()

                    )
            ))   //첫번째는 문서이름.
        ;
    }

    private String getBearerToken() throws Exception {
        return "Bearer "+ getAccessToken();
    }

    private String getAccessToken() throws Exception {
        //Given
        //default user가 있기 때문에 또 저장할 필요 없다.
        String username = "dragonhee.kim2@gmail.com";
        String password = "dydrkfl1";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username",username)
                .param("password", password)
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return parser.parseMap(responseBody).get("access_token").toString();

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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                  .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("_links.index").exists())
//                .andExpect(jsonPath("content[0].objectName").exists())
//                //.andExpect(jsonPath("$[0].field ").exists())
//                .andExpect(jsonPath("content[0].defaultMessage").exists())
//                .andExpect(jsonPath("content[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())

        ;
    }

    @Test
    @TestDescription("30개의 이벤트를  10개씩 두번씩 조회하기")
    public void queryEvents() throws Exception{
        //Given
        IntStream.range(0,30).forEach(this::generateEvent);

        //When && Then
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;


    }

    @Test
    @TestDescription("기존의 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when && then
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
    @TestDescription("없는 이벤트 조회시 404 리턴받기")
    public void getEvent404() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when && then
        this.mockMvc.perform(get("/api/events/1234"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @TestDescription("이벤트 정상적으로 수정")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        String eventName = "update event";
        eventDto.setName(eventName);

        //when then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = new EventDto();

        //when then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(1000);
        eventDto.setMaxPrice(200);

        //when then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(1);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //when then
        this.mockMvc.perform(put("/api/events/123")
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }


    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event "+index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,02,01,23,32))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,02,02,23,50))
                .beginEventDateTime(LocalDateTime.of(2021,02,03,23,50))
                .endEventDateTime(LocalDateTime.of(2021,02,04,23,30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역~")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventReposiroty.save(event);
    }


}
