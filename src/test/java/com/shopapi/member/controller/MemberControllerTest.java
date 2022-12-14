package com.shopapi.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapi.member.domain.Address;
import com.shopapi.member.domain.Member;
import com.shopapi.member.repository.MemberRepository;
import com.shopapi.member.request.MemberRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 저장 시 유효성 검증")
    void testRequestValidationWhenSaveMember() throws Exception {
        MemberRequest request = MemberRequest.builder()
                .name(null)
                .email(null)
                .password(null)
                .addressList(null)
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/members")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.name").value("잘못된 이름 값입니다."))
                .andExpect(jsonPath("$.validation.password").value("잘못된 패스워드 값입니다."))
                .andExpect(jsonPath("$.validation.email").value("잘못된 이메일 값입니다."))
                .andDo(print());
        // TODO: 2022/11/24 ExceptionHandlerController 와 ErrorResponse 직접 만들기
    }

    @Test
    @DisplayName("/members 요청시 회원 저장 테스트")
    void testSaveMember() throws Exception {
        //given
        List<Address> addresses = IntStream.range(0, 3)
                .mapToObj(i -> Address.builder()
                        .city("서울")
                        .street("종로구")
                        .zipCode("22456")
                        .build())
                .collect(Collectors.toList());

        MemberRequest request = MemberRequest.builder()
                .name("이름1")
                .email("1234@naver.com")
                .password("1234")
                .addressList(addresses)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/api/members")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then

        assertEquals(1L, memberRepository.count());
        Member member = memberRepository.findAll().get(0);
        assertEquals("이름1" , member.getName());
        assertEquals("1234@naver.com" , member.getEmail());
        assertEquals("1234" , member.getPassword());
//        assertEquals(3,member.getAddressList().size());

        /*
            현재 no session? Exception 발생
            addressRepository 만들고 다시 테스트
            @GeneratedValue strategy를 전략해야하는가?

            일부 Exception 메세지
            failed to lazily initialize a collection of role: com.shopapi.member.domain.Member.addressList,
            could not initialize proxy - no Session
            org.hibernate.LazyInitializationException:
            failed to lazily initialize a collection of role:
            com.shopapi.member.domain.Member.addressList, could not initialize proxy - no Session
         */
        // TODO: 2022/11/24 주소까지 검증
    }

    @Test
    @DisplayName("회원 단건 조회 테스트")
    void getMemberById() throws Exception {

        //given
        Member request = Member.builder()
                .name("테스트 이름")
                .email("1234@naver.com")
                .password("1234")
                .build();
        memberRepository.save(request);

        //expected
        mockMvc.perform(get("/api/members/{memberId}", request.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.name").value("테스트 이름"))
                .andExpect(jsonPath("$.email").value("1234@naver.com"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 다건 조회 페이징 처리")
    void getMemberListTestByPage () throws Exception {

        //given
        List<Member> requestList = IntStream.range(1, 31)
                .mapToObj(i -> Member.builder()
                        .name("테스트 이름 " + i)
                        .email(i + "@naver.com")
                        .password("1234" + i)
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(requestList);

        //expected
        mockMvc.perform(get("/api/members?page=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(5)))
                .andExpect(jsonPath("$.[0].id").value(requestList.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(requestList.get(0).getName()))
                .andExpect(jsonPath("$.[0].email").value(requestList.get(0).getEmail()))
                .andExpect(jsonPath("$.[0].password").value(requestList.get(0).getPassword()))
                .andDo(print());

    }
}