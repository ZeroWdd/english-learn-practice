package com.learn.web.util;

import org.springframework.stereotype.Component;

@Component
public class AjaxResult {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void ajaxFalse(String message){
        this.message = message;
        this.success = false;
    }

    public void ajaxSuccess(String message){
        this.message = message;
        this.success = true;
    }

    public AjaxResult ajaxFalse(){
        this.success = false;
        return this;
    }

    public AjaxResult ajaxSuccess(){
        this.success = true;
        return this;
    }
}