package cn.sduonline.sdu_lantern_festival_2021.entity;

import java.util.Collections;

public class Response {
    int code;
    String message;
    Object data = Collections.emptyMap();

    Response(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    Response(ResponseCode responseCode, Object data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }


    public static Response success() {
        return new Response(ResponseCode.SUCCESS);
    }

    public static Response success(Object data) {
        return new Response(ResponseCode.SUCCESS, data);
    }

    public static Response fail(ResponseCode responseCode) {
        return new Response(responseCode);
    }

    public static Response fail(ResponseCode responseCode, Object data) {
        return new Response(responseCode, data);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
