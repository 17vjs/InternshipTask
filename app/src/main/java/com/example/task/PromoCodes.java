package com.example.task;

public class PromoCodes {
    String code;
    String validity;

    public PromoCodes() {
    }

    public PromoCodes(String code, String validity) {
        this.code = code;
        this.validity = validity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
