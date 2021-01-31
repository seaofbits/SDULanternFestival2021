package cn.sduonline.sdu_lantern_festival_2021.restcontroller;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@ResponseBody
public class ParameterInvalidExceptionHandler {
    @ExceptionHandler({MissingServletRequestParameterException.class})
    String MissingParameter() {
        return "MissingServletRequestParameterException";
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    String ParameterTypeError() {
        return "MethodArgumentTypeMismatchException";
    }
}
