package com.orelit.springcore.common.exception;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.tomcat.ConnectorStartFailedException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller advice that handles all exception handling. This includes logging exception messages
 * and formatting error responses.
 */
@ControllerAdvice
@Slf4j
public class ErrorHandler {

    public static final String UNKNOWN_MESSAGE = "Unhandled exception";

    public static final String VALIDATION_MESSAGE = "Validation failed";

    /**
     * Handles a few validation failure exceptions. Extract the property paths that caused the exception.
     *
     * @param ex The exception
     * @return ErrorMessageDto with all the properties included in the exception
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            ConnectorStartFailedException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorMessageDto> handleValidationException(Exception ex) {
        ErrorMessageDto message = new ErrorMessageDto(VALIDATION_MESSAGE, ErrorMessageUtil.getLogRef(),
                ValidationErrorUtil.extractFieldGenerics(ex));
        message.setType(ex.getClass().getSimpleName());

        log.warn(message.prettyPrint(), ex);
        return ErrorMessageUtil.createResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles the {@link PropertyValidationException} manually thrown by us.
     *
     * @param ex {@link PropertyValidationException} with property path and messages
     * @return ErrorMessageDto with all the properties included in the exception
     */
    @ExceptionHandler({PropertyValidationException.class})
    public ResponseEntity<ErrorMessageDto> handlePropertyValidationException(PropertyValidationException ex) {
        ErrorMessageDto message = new ErrorMessageDto(VALIDATION_MESSAGE, ErrorMessageUtil.getLogRef(),
                ex.getValidationErrors());
        message.setType(ex.getClass().getSimpleName());

        log.warn(message.prettyPrint(), ex);
        return ErrorMessageUtil.createResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles {@link ResponseStatusException}. The status code and reason are extracted from the exception.
     *
     * @param ex The exception
     * @return ErrorMessageDto with status code extracted from exception
     */
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ErrorMessageDto> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason();
        log.warn(message, ex);
        //ErrorMessageUtil.createResponse(message, ex.get, ex);-TODO
        return ErrorMessageUtil.createResponse(message);
    }

    /**
     * Handles {@link HttpStatusCodeException} This is thrown by this API getting an error from another API,
     * so we cannot handle it here.
     *
     * @param ex The exception
     * @return ErrorMessageDto with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler({HttpStatusCodeException.class})
    public ResponseEntity<ErrorMessageDto> handleHttpStatusCodeException(HttpStatusCodeException ex) {
        String message = String.format("HTTp %d - %s", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        log.warn(message, ex);
        return ErrorMessageUtil.createResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /**
     * Handles Spring's {@link MissingServletRequestParameterException}.
     *
     * @param ex The exception
     * @return ErrorMessageDto with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorMessageDto> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter [400] - " + ex.getMessage(), ex);
        return ErrorMessageUtil.createResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Throws a 400 Bad request when caching an {@link IllegalArgumentException}.
     *
     * @param ex The exception
     * @return ErrorMessageDto with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorMessageDto> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.warn("Illegal argument [400] - " + ex.getMessage(), ex);
        return ErrorMessageUtil.createResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Handle our {@link NotFoundException}.
     *
     * @param ex The exception
     * @return ErrorMessageDto with status 404 (Not Found)
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorMessageDto> handleNotFoundException(
            NotFoundException ex) {
        log.warn("Request not found [404] - %s".formatted(ex.getLocalizedMessage()), ex);
        return ErrorMessageUtil.createResponse(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Handles {@link OptimisticLockingFailureException} that get thrown when the supplied version is incorrect
     * for an update/delete.
     *
     * @param ex The exception thrown by Hibernate and Spring JPA
     * @return ErrorMessageDto with status 409 (Conflict)
     */
    @ExceptionHandler({OptimisticLockingFailureException.class})
    public ResponseEntity<ErrorMessageDto> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException ex) {
        log.warn("OptimisticLockingFailure [409] - %s".formatted(ex.getLocalizedMessage()), ex);
        ErrorMessageDto errorMessage = new ErrorMessageDto(
                ex.getLocalizedMessage(), ErrorMessageUtil.getLogRef());
        return ErrorMessageUtil.createResponse(errorMessage, HttpStatus.CONFLICT);
    }

    /**
     * If no specific error handler was defined, it will fall through to here and fail hard.
     *
     * @param ex The exception
     * @return ErrorMessageDto with the exception detail and status 500 (Internal Server Error)
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorMessageDto> handleGenericException(Exception ex) {
        String message = UNKNOWN_MESSAGE + ": " + ex.getClass().getSimpleName();

        if(StringUtils.isNotBlank(ex.getLocalizedMessage())) {
            message += " - " + ex.getLocalizedMessage();
        }
        log.warn(message, ex);
        return ErrorMessageUtil.createResponse(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
