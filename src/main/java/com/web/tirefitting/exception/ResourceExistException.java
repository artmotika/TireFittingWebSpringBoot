package com.web.tirefitting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceExistException extends RuntimeException {
    private static final long serialVersionUID = 2L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceExistException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exist %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
