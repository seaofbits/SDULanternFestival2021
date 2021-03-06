package cn.sduonline.sdu_lantern_festival_2021.restcontroller;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({MissingServletRequestParameterException.class})
    Response MissingParameter() {
        return Response.fail(ResponseCode.ERROR_MISSING_PARAMETER);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    Response ParameterTypeMismatch() {
        return Response.fail(ResponseCode.ERROR_PARAMETER_TYPE_MISMATCH);
    }

    @ExceptionHandler({Exception.class})
    Response Exception(Exception e) {
        logger.error("unknown error", e);
        return Response.fail(ResponseCode.ERROR_UNKNOWN, e.getMessage());
    }
}
