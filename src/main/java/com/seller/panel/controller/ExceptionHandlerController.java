package com.seller.panel.controller;

import com.seller.panel.dto.ApiError;
import com.seller.panel.exception.SellerPanelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

@ControllerAdvice
@RestController
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final Log LOG = LogFactory.getLog(ExceptionHandlerController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex, WebRequest req) {
        LOG.error("Exception occurred", ex);
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setDateTime(Instant.now());
        error.setMessageKey("server.error");
        error.setMessage(ex.getLocalizedMessage());
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getLocalizedMessage());
        }
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(Exception ex, WebRequest req) {
        LOG.error("AccessDeniedException occurred", ex);
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setDateTime(Instant.now());
        error.setMessageKey("access.denied");
        error.setMessage(ex.getLocalizedMessage());
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getLocalizedMessage());
        }
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(SellerPanelException.class)
    public ResponseEntity<ApiError> handleApplicationException(Exception ex, WebRequest req) {
        LOG.error("SellerPanelException occurred: ", ex);
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setDateTime(Instant.now());
        error.setMessageKey(((SellerPanelException) ex).getMessageKey());
        error.setMessage(ex.getLocalizedMessage());
        if (ex.getCause() != null) {
            error.setCause(ex.getCause().getLocalizedMessage());
        }
        return new ResponseEntity<>(error, error.getStatus());
    }

}
