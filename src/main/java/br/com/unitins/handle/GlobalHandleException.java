package br.com.unitins.handle;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URISyntaxException;


import br.com.unitins.exception.ErrorResponse;
import br.com.unitins.handle.exceptionhandle.ExceptionHandler;

@Provider
public class GlobalHandleException implements ExceptionMapper<Exception> {

    @Context
    HttpServerRequest request;

    @SneakyThrows
    @Override
    public Response toResponse(Exception exception) {
        ErrorResponse errorResponse = buildResponse(exception);
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }

    private ErrorResponse buildResponse(Exception exception) throws URISyntaxException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(new URI(""))
                .instance(new URI(request.absoluteURI()))
                .build();

        ExceptionHandler handler = ExceptionHandlerRegistry.getHandler(exception.getClass());
        handler.handleException(exception, errorResponse);

        return errorResponse;
    }
}

