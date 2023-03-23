package br.com.unitins.handler;

import br.com.unitins.exception.ErrorResponse;
import br.com.unitins.service.mail.EmailService;
import io.vertx.core.http.HttpServerRequest;
import lombok.SneakyThrows;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class GenericExceptionMapper {

    @Inject
    EmailService emailService;

    @Context
    HttpServerRequest request;

    @SneakyThrows
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapException(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(new URI(""))
                .title("Internal Server Error")
                .detail(exception.getMessage())
                .instance(new URI(request.absoluteURI()))
                .status(RestResponse.StatusCode.INTERNAL_SERVER_ERROR)
                .build();

        emailService.buildAndSend(exception);

        return RestResponse.status(INTERNAL_SERVER_ERROR, errorResponse);
    }

    @SneakyThrows
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(new URI(""))
                .title("Validation Error")
                .detail(exception.getMessage())
                .instance(new URI(request.absoluteURI()))
                .status(RestResponse.StatusCode.BAD_REQUEST)
                .build();

        return RestResponse.status(RestResponse.Status.BAD_REQUEST, errorResponse);
    }

    @SneakyThrows
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapException(NotFoundException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(new URI(""))
                .title("Not found")
                .detail(exception.getMessage())
                .instance(new URI(request.absoluteURI()))
                .status(RestResponse.StatusCode.BAD_REQUEST)
                .build();

        return RestResponse.status(RestResponse.Status.BAD_REQUEST, errorResponse);
    }
}