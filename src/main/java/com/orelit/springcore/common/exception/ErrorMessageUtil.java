package com.orelit.springcore.common.exception;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;

/**
 * Utility methods for creating {@link ErrorMessageDto}
 */
public class ErrorMessageUtil {
    private ErrorMessageUtil(){}

    /**
     * Create the logref. Useful as correlation identifier.
     *
     * @return A correlation identifier to use with the error.
     */
    public static String getLogRef() {
        String logReference = MDC.get("traceId");

        //Generate a random token if no correlation exists.
        if(logReference == null || logReference.isBlank()) {
            logReference = generateToken(20);
        }
        return logReference;
    }

    private static final SecureRandom RANDOM = new SecureRandom();

    private static String generateToken(int length) {
        StringBuilder sb = new StringBuilder();

        String sourceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        char[] source = sourceString.toCharArray();
        for (int i = 0; i<length; i++) {
            sb.append(source[RANDOM.nextInt(source.length)]);
        }
        return sb.toString();
    }

    /**
     * Convenience method to get caller without detail.
     *
     * @see #getCaller(StackTraceElement[], boolean)
     * @param stackTrace Array of StackTraceElement. Get this from {@link Thread#getStackTrace()}
     *                  or from {@link Exception#getStackTrace()}.
     * @return String with class and method name.
     */
    private static String getCaller(StackTraceElement[] stackTrace) {

        return getCaller(stackTrace, false);
    }

    /**
     * Get the class name of the latest element in the stack trace that was part of out package,
     * but not part of the error generation.
     *
     * @param stackTrace Array of StackTraceElement. Get this from {@link Thread#getStackTrace()}
     *                  or from {@link Exception#getStackTrace()}.
     * @param includeDetails Include the method name and line number ?
     * @return String with class and method name with optional details like line number.
     */
    private static String getCaller(StackTraceElement[] stackTrace, boolean includeDetails) {
        StackTraceElement caller = null;

        for (StackTraceElement element : stackTrace) {
            // Get the latest stack trace element that is in our package but not part of the error
            // message or error generation.
            if((element.getClassName().startsWith("com.sinorbis.bifrost"))
                    && (!element.getClassName().contains(".error."))
            ){
                caller = element;
                break;
            }
        }

        if(caller == null) {
            return null;
        }

        if(includeDetails) {
            return "%s.%s:%d".formatted(caller.getClassName(), caller.getMethodName(), caller.getLineNumber());
        } else {
            final String className = caller.getClassName();
            final String[] split = className.split("\\.");
            if (split.length > 0) {
                return split[split.length - 1];
            } else {
                return "%s.%s".formatted(className, caller.getMethodName());
            }
        }
    }

    public static final HttpStatus  DEFAULT_HTTP_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    public static final String DEFAULT_CONTENT_TYPE = "application/json";

    /**
     * Create a {@link ResponseEntity} with message.
     *
     * @param errorMessage Error message.
     * @return {@link ResponseEntity} with message.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final String errorMessage) {
        return createResponse(new ErrorMessageDto(errorMessage, getLogRef()), DEFAULT_HTTP_STATUS,
                DEFAULT_CONTENT_TYPE);
    }

    /**
     * Create a {@link ResponseEntity} with message and HTTP status.
     *
     * @param errorMessage Error message.
     * @param exception Exception that caused the error.
     * @return ResponseEntity with message and type.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final String errorMessage,
                                                                 final Throwable exception) {
        ResponseEntity<ErrorMessageDto> responseEntity =  createResponse(errorMessage);
        ErrorMessageDto body = responseEntity.getBody();
        if (exception != null && body != null) {
            body.setType(exception.getClass().getSimpleName());
        }
        return responseEntity;
    }

    /**
     * Create a {@link ResponseEntity} with message.
     *
     * @param errorMessage Error message.
     * @return {@link ResponseEntity} with message.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final ErrorMessageDto errorMessage) {
        return createResponse(errorMessage, DEFAULT_HTTP_STATUS, DEFAULT_CONTENT_TYPE);
    }

    /**
     * Create a {@link ResponseEntity} with message and HTTP status.
     *
     * @param errorMessage Error message.
     * @param httpStatus HTTP status code.
     * @return {@link ResponseEntity} with message and status.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final String errorMessage,
                                                                 final HttpStatus httpStatus) {
        return createResponse(new ErrorMessageDto(errorMessage, getLogRef()), httpStatus, DEFAULT_CONTENT_TYPE);
    }

    /**
     * Create a {@link ResponseEntity} with message and HTTP status.
     *
     * @param errorMessage Error message.
     * @param httpStatus HTTP status code.
     * @param exception Exception that caused the error.
     * @return ResponseEntity with message, status and type.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final String errorMessage, final HttpStatus httpStatus,
                                                                 final Throwable exception) {
        ResponseEntity<ErrorMessageDto> responseEntity =  createResponse(errorMessage, httpStatus);
        ErrorMessageDto body = responseEntity.getBody();
        if (exception != null && body != null) {
            body.setType(exception.getClass().getSimpleName());
        }
        return responseEntity;
    }


    /**
     * Wraps a created {@link ErrorMessageDto} in a {@link ResponseEntity}.
     *
     * @param errorMessage {@link ErrorMessageDto}
     * @param httpStatus HTTP status code.
     * @return ResponseEntity for error message.
     */
    public static ResponseEntity<ErrorMessageDto> createResponse(final ErrorMessageDto errorMessage,
                                                                 final HttpStatus httpStatus) {
        return createResponse(errorMessage, httpStatus, DEFAULT_CONTENT_TYPE);
    }

    public static ResponseEntity<ErrorMessageDto> createResponse(final ErrorMessageDto errorMessage,
          final HttpStatus httpStatus, final String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", contentType);
        return new ResponseEntity<>(errorMessage, headers, httpStatus);
    }

}
