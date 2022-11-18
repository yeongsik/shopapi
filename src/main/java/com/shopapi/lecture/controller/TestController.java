package com.shopapi.lecture.controller;

import com.shopapi.lecture.request.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/api/hello")
    public String get() {
        return "Hello";
    }

    @PostMapping("/api/helloPost")
    public String post(@RequestParam Map<String , String> params) {
        log.info("params={}", params);
        return "Hello";
    }

    @PostMapping("/api/helloPostClass")
    public String postRequestClass(@ModelAttribute Test requestTest) {
        log.info("requestTest = {}" , requestTest.toString());
        return "Hello";
    }

    @PostMapping("/api/helloPostJson")
    public Map<String, String> postRequestJson(@RequestBody @Valid Test requestTest, BindingResult result) throws Exception {
        /*
            데이터 검증을 하는 이유
            1. client 개발자가 깜빡할 수 있다. -> 실수로 값을 안 보낼 수 있음
            2. client bug ( 개발자 실수 ) 어떤 버그로 인해서 값이 누락 될 수 있음
            3. 외부에서 값을 임의로 조작해서 보낼 수 있다.
            4. DB에 값을 저장할 때 의도치 않은 오류가 발생 할 수 있다.
            5. 서버 개발자의 편안함을 위해서 ( 검증이 된 데이터기 때문에 로직을 진행하는 데 편하다 )
         */
        log.info("requestTest = {}" , requestTest.toString());
        /*
            1.데이터가 수십가지 일 경우 이런 검증은 빡세다 -> 노가다 작업
            2.무언가 3번 이상 반복작업을 하면 의심하자
            3.누락 가능성
            4.생각보다 검증해야할 게 많다. ( 꼼꼼하지 않을 수 있다 )
                제이슨의 타이틀 값이
                {"title" : "         "} 이렇게 넘어올 경우
                {"title" : ".....................수십억 글자"}

//        if (title == null || title.equals("")) {
//            // error
//            throw new Exception("타이틀 값이 없어요");
//        }
//        String content = requestTest.getContent();
//
//        if (content == null || content.equals("")) {
//            // error
//        }
              Validation 라이브러리 @NotBlank를 사용
              스프링 부트 특정 버전 2.3 부터 web-starter 라이브러리에 포함 X
              validation을 따로 추가 해줘야 한다.

              @Valid 를 @RequestBody에 붙이면 요청 파라미터가 넘어오기 전에 검증해준다.
            5.개발자스럽지 않다
         */

        // 사용자에게 에러를 전달해주고 싶은데 valid를 사용하면 컨트롤러로 넘어오기 전에 Exception 발생
        if (result.hasErrors()) {
            /*
                이런 방식 문제
                1.매번 메서드마다 검증 코드가 들어 가야 한다.
                    > 개발자가 까먹을 수 있다.
                    > 검증 부분에서 버그 발생할 수도 있다.
                2.응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
                3.여러개의 에러처리가 힘듦
                4.반복 작업 피하기
                    > 코드 && 개발에 관한 모든 것 ( ex : 개발 프로세스 , 인프라 설정 등 )
                    > 자동화 방법 생각하기

                이런 문제점을 컨트롤러 어드바이스를 사용해서 해결
             */
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); // title
            String errorMessage = firstFieldError.getDefaultMessage();// .. 에러 메시지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }
        return Map.of();
    }

    @PostMapping("/api/validationByControllerAdvice")
    public Map<String, String> validationByControllerAdvice(@RequestBody @Valid Test requestTest) throws Exception {
        log.info("requestTest = {}" , requestTest.toString());
        return Map.of();
    }
}