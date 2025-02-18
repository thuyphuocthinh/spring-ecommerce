package com.tpt.tpt_ecom.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String field, String fieldName, String resourceName) {
        // super(message)
        super(String.format("Field %s not found in resource %s.", fieldName, resourceName));
        this.field = field;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        // super(message)
        super(String.format("Field %s not found in resource %s.", field, resourceName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
