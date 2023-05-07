package br.com.unitins.handle.exceptionhandle;

import br.com.unitins.exception.ConstraintViolationResponse;
import br.com.unitins.exception.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Set;

public class ConstraintViolationExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(Exception exception, ErrorResponse errorResponse) {
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();

        errorResponse.setTitle(getTitle());
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setStatus(getStatus());
        ((ConstraintViolationResponse) errorResponse).setViolations(constraintViolations);
    }

    @Override
    public Class<? extends Exception> getExceptionType() {
        return ConstraintViolationException.class;
    }

    @Override
    public String getTitle() {
        return "Constraint Violation";
    }

    @Override
    public int getStatus() {
        return RestResponse.StatusCode.BAD_REQUEST;
    }
}
