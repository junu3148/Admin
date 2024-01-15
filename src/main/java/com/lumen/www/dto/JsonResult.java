package com.lumen.www.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JsonResult {
    //필드
    private String result; /* 'success' or 'fail' */
    private Object data; /*  성공했을 때(result=='success') data */
    private String failMsg; /* 실패했을 때 result = fail 일 때 참고할 수 있는 메세지

		/* 생성자*/

    public JsonResult() {
        super();
    }

    public JsonResult(String result, Object data, String failMsg) {
        super();
        this.result = result;
        this.data = data;
        this.failMsg = failMsg;
    }

    //성공했을 시 사용하는 메소드
    public void success(Object data) {
        this.result = "success";
        this.data = data;
    }

    //실패했을 때 사용하는 메소드
    public void fail(String msg) {
        this.result = "fail";
        this.failMsg = msg;
    }

}