package com.morpheo.cursomc.resources.controllers.exception;

import java.io.Serializable;

public class FieldMessage implements Serializable {
    private String fieldName;
    private String message;

    public FieldMessage() {
    }

    public FieldMessage(String fildName, String message) {
        this.fieldName = fildName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
