package com.bbende.project.starter.web.mvc;

import org.springframework.http.HttpHeaders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Unpoly {

    public static final String UNPOLY = "unpoly";

    public static final String X_UP_TARGET = "X-Up-Target";
    public static final String X_UP_VALIDATE = "X-Up-Validate";

    private final String xUpTarget;
    private final String xUpValidate;

    public Unpoly(final String xUpTarget, final String xUpValidate) {
        this.xUpTarget = xUpTarget;
        this.xUpValidate = xUpValidate;
    }

    public String getXUpTarget() {
        return xUpTarget;
    }

    public boolean isXUpTarget() {
        return xUpTarget != null && !xUpTarget.trim().isEmpty();
    }

    public String getXUpValidate() {
        return xUpValidate;
    }

    public boolean isXUpValidate() {
        return xUpValidate != null && !xUpValidate.trim().isEmpty();
    }

    public String getView(final String viewName) {
        return isXUpTarget() ? viewName + " :: " + getXUpTarget() : viewName;
    }

    public BindingResult getFieldBinding(final BindingResult objectBinding) {
        final FieldError fieldError = objectBinding.getFieldError(getXUpValidate());
        final BindingResult fieldBinding = new BeanPropertyBindingResult(
                objectBinding.getTarget(), objectBinding.getObjectName());
        fieldBinding.addError(fieldError);
        return fieldBinding;
    }

    public static Unpoly fromHeaders(final HttpHeaders headers) {
        final String xUpTarget = headers.getFirst(Unpoly.X_UP_TARGET);
        final String xUpValidate = headers.getFirst(Unpoly.X_UP_VALIDATE);
        return new Unpoly(xUpTarget, xUpValidate);
    }
}
