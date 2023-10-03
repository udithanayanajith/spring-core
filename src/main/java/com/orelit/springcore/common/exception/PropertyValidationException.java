package com.orelit.springcore.common.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An exception that contains a map of properties that failed validations, each with their own error
 * message. This works alongside the Spring Validation exception like ConstraintViolationException,
 * but we are throwing it manually.
 */
public class PropertyValidationException extends RuntimeException {

  private final Map<String, String> validationErrors;

  public PropertyValidationException(final Map<String, String> errors) {
    super("Validation failed: " + Arrays.toString(errors.entrySet().toArray()));
    validationErrors = new HashMap<>(errors);
  }

  public PropertyValidationException(String field, String filedMessage) {
    this(Collections.singletonMap(field, filedMessage));
  }

  public Map<String, String> getValidationErrors() {
    return validationErrors;
  }
}
