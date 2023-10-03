package com.orelit.springcore.common.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility methods ot handle different kinds of validation exceptions.
 */
@Slf4j
public class ValidationErrorUtil extends RuntimeException {

    public static final String INVALID_INPUT = "Invalid input: ";

    private static String removeNestedExceptionMessage(String message) {
        log.trace(String.format("Cleaning message '%s'", message));

        if(message != null && !message.isEmpty()) {
            final int index = message.indexOf("; nested exception is");
            if(index > 1 && index < message.length()) {
                return message.substring(0, index);
            }
        }
        return message;
    }

    private static String getConstraintViolationMessageWithoutPropertyPath(
            ConstraintViolation<?> constraintViolation) {
        log.debug(ReflectionToStringBuilder.toString(constraintViolation, ToStringStyle.SHORT_PREFIX_STYLE));
        if(constraintViolation != null) {
            final String message = constraintViolation.getMessage() != null
                    ? constraintViolation.getMessage() : "Constraint violation";

            final Path propertyPath = constraintViolation.getPropertyPath();

            if (propertyPath != null) {
                final String pathString = propertyPath.toString();
                final String prefix = pathString + ": ";
                if (message.startsWith(prefix)) {
                    return message.substring(prefix.length());
                }
            }
            return message;
        }
        return "";
    }

    private static String getConstraintViolationPropertyName(ConstraintViolation<?> violation) {
        log.debug(ReflectionToStringBuilder.toString(violation, ToStringStyle.SHORT_PREFIX_STYLE));
        if(violation != null) {
            String leafNode = "";
            for (Path.Node next : violation.getPropertyPath()) {
                if(next != null) {
                    leafNode = next.getName();
                }
            }
            return leafNode;
        }
        return "";
    }

    /**
     * Try to extract fields with the messages from a validation exception.
     *
     * @see ValidationErrorUtil#extractFields(ConstraintViolationException)
     * @see ValidationErrorUtil#extractFields(MethodArgumentNotValidException)
     * @see ValidationErrorUtil#extractFields(MethodArgumentTypeMismatchException)
     * @see ValidationErrorUtil#extractFields(HttpMessageNotReadableException)
     * @param ex The exception containing validation failures
     * @return A dictionary with the field name as key and error messages as value
     * @throws RuntimeException if the exception type is not recognized
     */
    public static Map<String, String> extractFieldGenerics(Exception ex) {
        if(ex instanceof MethodArgumentNotValidException) {
            return ValidationErrorUtil.extractFields((MethodArgumentNotValidException) ex);
        } else if (ex instanceof ConstraintViolationException) {
            return ValidationErrorUtil.extractFields((ConstraintViolationException) ex);
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            return ValidationErrorUtil.extractFields((MethodArgumentTypeMismatchException) ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return ValidationErrorUtil.extractFields((HttpMessageNotReadableException) ex);
        } else {
            throw new RuntimeException(String.format("Unknown exception type '%s'", ex.getClass().getSimpleName()));
        }
    }

    /**
     * Extract fields with messages from a {@link MethodArgumentNotValidException}.
     *
     * @param ex The exception containing validation failures
     * @return A directory with the field name as key and error message as value
     */
    public static Map<String, String> extractFields(MethodArgumentNotValidException ex) {
        Map<String, String> result = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            flattenMultimap(result, fieldError.getField(), fieldError.getDefaultMessage());
        }
        return result;
    }

    /**
     * Extract fields with messages from a {@link ConstraintViolationException}.
     *
     * @param ex The exception containing violation failures
     * @return A dictionary with the field name as key and error message as value
     */
    public static Map<String, String> extractFields(ConstraintViolationException ex) {
        Map<String, String> result = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            flattenMultimap(result, getConstraintViolationPropertyName(violation),
                    removeNestedExceptionMessage(getConstraintViolationMessageWithoutPropertyPath(violation)));
        }
        return result;
    }

    /**
     * Extract fields with messages from a {@link HttpMessageNotReadableException}.
     *
     * @param ex The exception containing validation failures
     * @return A dictionary with the field name as key and error messages as value
     */
    public static Map<String, String> extractFields(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if(cause instanceof InvalidFormatException) {
            InvalidFormatException ife = ((InvalidFormatException) cause);
            final String fieldPath = builFieldPathString(ife.getPath());
            return Collections.singletonMap(fieldPath, INVALID_INPUT + ife.getValue());
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) cause;
            final String fieldPath = builFieldPathString(mie.getPath());
            return Collections.singletonMap(fieldPath, INVALID_INPUT + mie.getOriginalMessage());
        } else if (cause instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) cause;
            final String location = String.format("Line %d:%d", jpe.getLocation().getLineNr(),
                    jpe.getLocation().getColumnNr());
            return Collections.singletonMap(location, INVALID_INPUT + jpe.getOriginalMessage());
        }
        return Collections.singletonMap("", ex.getMessage());
    }

    public static Map<String, String> extractFields(MethodArgumentTypeMismatchException ex) {
        Map<String, String> result = new HashMap<>();
        String propertyName = ex.getPropertyName();

        if(propertyName == null) {
            try {
                propertyName = ex.getName();
            } catch (Exception inner) {
                log.debug("Could not method argument name", inner);
            }
        }
        result.put(propertyName, removeNestedExceptionMessage(ex.getMessage()));
        return result;
    }

    private static void flattenMultimap(final Map<String, String> result, final String key, final String value) {
        if(result.containsKey(key)) {
            String oldValue = result.get(key);
            if(!oldValue.contains(value)) {
                result.replace(key, oldValue + ", " + value);
            }
        } else {
            result.put(key, removeNestedExceptionMessage(value));
        }
    }

    public static String builFieldPathString (List<Reference> pathList) {
        if(pathList == null) {
            return null;
        }

        StringBuilder sb  = new StringBuilder();

        for (Reference reference : pathList) {
            final String fieldName = reference.getFieldName();
            if(fieldName != null) {
                if(sb.length() > 0) {
                    sb.append(".");
                }
                sb.append(fieldName);
            } else if (reference.getIndex() >= 0) {
                sb.append("[" + reference.getIndex() + "]");
            } else {
                log.debug("Unknown Json Reference type: " + ReflectionToStringBuilder.toString(reference));
            }
        }
        return sb.toString();
    }
}
