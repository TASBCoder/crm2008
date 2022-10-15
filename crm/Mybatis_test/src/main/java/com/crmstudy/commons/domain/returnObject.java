package com.crmstudy.commons.domain;

public class returnObject {
    private String code;
    private String message;
    private Object returnOther;

    public returnObject() {
    }

    public returnObject(String code, String message, Object returnOther) {
        this.code = code;
        this.message = message;
        this.returnOther = returnOther;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnOther() {
        return returnOther;
    }

    public void setReturnOther(Object returnOther) {
        this.returnOther = returnOther;
    }

    @Override
    public String toString() {
        return "returnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", returnOther=" + returnOther +
                '}';
    }
}
