package br.com.unitins.exception;

import lombok.*;

import java.net.URI;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private URI type;
    private String title;
    private int status;
    private String detail;
    private URI instance;
}