package com.rookies5.MySpringbootLab.advice;

import com.rookies5.MySpringbootLab.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError; // ✨ 새로 추가된 import (필드 에러 확인용)
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException; // ✨ 새로 추가된 import (유효성 검증 에러 클래스)
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionAdvice {

//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<ErrorObject> handleResourceNotFoundException(BusinessException ex) {
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(ex.getHttpStatus().value());
//        errorObject.setMessage(ex.getMessage());
//
//        log.error(ex.getMessage(), ex);
//
//        //ResponseEntity = body + statuscode + header
//        return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(ex.getHttpStatus().value()));
//    }

    /*
        Spring6 버전에 추가된 ProblemDetail 객체에 에러정보를 담아서 리턴하는 방법
     */
    @ExceptionHandler(BusinessException.class)
    protected ProblemDetail handleException(BusinessException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(e.getMessage());
        //사용자가 임의로 정의하는 에러코드와 값
        problemDetail.setProperty("errorCategory", "Generic");
        problemDetail.setProperty("timestamp",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss E a", Locale.KOREA)
                        .format(LocalDateTime.now()));
        return problemDetail;
    }

    //숫자타입의 값에 문자열타입의 값을 입력으로 받았을때 발생하는 오류
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", e.getMessage());
        result.put("httpStatus", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // =================================================================
    // ✨ 새로 추가된 부분: @Valid 유효성 검증 실패 시 발생하는 예외 처리
    // =================================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 1. 발생한 모든 검증 에러를 돌면서 "필드명: 에러메시지" 형태로 모아줍니다.
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 2. 클라이언트에게 돌려줄 JSON 응답 형태를 예쁘게 조립합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value()); // 400 Bad Request
        response.put("message", "입력값이 올바르지 않습니다.");
        response.put("errors", errors); // 구체적으로 어떤 필드가 틀렸는지 목록 제공

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}