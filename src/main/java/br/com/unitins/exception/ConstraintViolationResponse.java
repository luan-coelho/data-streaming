package br.com.unitins.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintViolationResponse extends ErrorResponse {

    private Set<ConstraintViolation<?>> violations;
}