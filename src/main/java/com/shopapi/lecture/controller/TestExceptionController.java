package com.shopapi.lecture.controller;

import com.shopapi.lecture.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class TestExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
//        MethodArgumentNotValidException
        log.error("exceptionHandler error", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse inValidRequestHandler(MethodArgumentNotValidException e) {

//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String errorMessage = fieldError.getDefaultMessage();
//
//        Map<String, String> response = new HashMap<>();
//        response.put(field, errorMessage);
//        // Json 형태로 작동 X
//
//        // Map으론 일반적인 컨트롤러처럼 뷰 리졸버를 찾는 상황 > ModelAndView 데이터를 넘겨준다.
//        // @ResponseBody로 해결
//        return response;

        // 에러 응답 클래스 사용
        ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        // code 나 메세지 값들이 변경시 테스트 코드에서도 맞춰줘야하는 개선 점이 있다.

        return response;
    }

}
