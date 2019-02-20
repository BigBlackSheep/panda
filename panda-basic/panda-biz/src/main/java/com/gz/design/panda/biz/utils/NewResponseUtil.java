package com.gz.design.panda.biz.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewResponseUtil<T> {

    public static final String SUCCESS = "1";
    public static final String FAILED = "0";
    public static final String SUCCEED = "success";
    public static final String FAILURE = "error";


    private String code;
    private T data;
    private String desc;

    private NewResponseUtil(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static NewResponseUtil newSucceedResponse() {
        return new NewResponseUtil(SUCCESS, SUCCEED);
    }


    public static NewResponseUtil newFailureResponse() {
        return new NewResponseUtil(FAILED, FAILURE);
    }


    public static NewResponseUtil newSucceedResponse(Object data) {
        return new NewResponseUtil(SUCCESS, data, SUCCEED);
    }


    public static NewResponseUtil newFailureResponse(Object data) {
        return new NewResponseUtil(FAILED, data, FAILURE);
    }


    public static NewResponseUtil newSucceedResponse(String desc) {
        return new NewResponseUtil(SUCCESS, desc);
    }

    public static NewResponseUtil newSucceedResponse(Object data, String desc) {
        return new NewResponseUtil(SUCCESS, data, desc);
    }

    public static NewResponseUtil newFailureResponse(String desc) {
        return new NewResponseUtil(FAILED, desc);
    }

    public static NewResponseUtil newFailureResponse(String code, String desc) {
        return new NewResponseUtil(code, desc);
    }

    public static NewResponseUtil newSucceedResponseData(Object data) {
        return new NewResponseUtil(SUCCESS, data, SUCCEED);
    }

    public static NewResponseUtil newFailureResponse(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            stringBuilder.append(objectError.getDefaultMessage()).append(";");
        }
        return new NewResponseUtil(FAILED, stringBuilder.toString());
    }
}
