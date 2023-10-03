package com.orelit.springcore.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generic container for error messages. It allows multi levels of error messages.
 */
public class ErrorMessageDto {

  /**
   * Error message to display. Example : Could not create Object
   */
  protected String message;

  /**
   * OPTIONAL (but recommended) Unique reference for this error or correlation ID if it exists.
   * Example: ER-1234
   */
  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected String logref;

  /**
   * OPTIONAL Property path of this error or field.Used when a specific property is at fault, for
   * instance validation. Example: person.firstName
   */
  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected String path;

  /**
   * OPTIONAL Sub-errors attached to this error response. Example: A list of validation messages
   * attached to a Validation Failed exception.
   */
  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  protected List<ErrorMessageDto> errors = new ArrayList<>();

  /**
   * OPTIONAL A code for this error type, if one exists. Example: ACCESS_DENIED
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected String code;

  /**
   * OPTIONAL The type of error that was thrown. This will most likely be the ClassName of the
   * exception. Example: NullPointerException
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected String type;


  /**
   * Adds inner error to this container message.
   *
   * @param errors - Child errors to add
   * @return The parent instance to allow fluid flow
   */
  public ErrorMessageDto addErrors(ErrorMessageDto... errors) {
    if (errors == null || errors.length == 0) {
      return this;
    }
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.addAll(Arrays.asList(errors));
    return this;
  }

  /**
   * Format the error message in a human-readable format.
   *
   * @return A human-readable version of the error message.
   */
  public String prettyPrint() {
    StringBuilder sb = new StringBuilder();

    if (path != null && !path.isBlank()) {
      sb.append(path);
    }
    if (message != null && !message.isBlank()) {
      if (sb.length() > 0) {
        sb.append("=");
      }
      sb.append(message);
    }

    if (errors != null && !errors.isEmpty()) {
      if (sb.length() > 0) {
        sb.append(" { ");
      }
      boolean first = true;
      for (ErrorMessageDto item : errors) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }
        sb.append(item.prettyPrint());
      }
      sb.append(" }");
    }
    return sb.toString();
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getLogref() {
    return logref;
  }

  public void setLogref(String logref) {
    this.logref = logref;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<ErrorMessageDto> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorMessageDto> errors) {
    this.errors = errors;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ErrorMessageDto() {
    //required by the Jackson
  }

  /**
   * Error message block.
   *
   * @param message message.
   * @param logRef  logRef.
   */
  public ErrorMessageDto(final String message, final String logRef) {
    if (message == null || message.isBlank()) {
      this.message = "Empty";
    } else {
      this.message = message;
    }
    this.logref = logRef;
  }

  /**
   * Error message block.
   *
   * @param message     message.
   * @param logRef      logRef.
   * @param fieldErrors fieldErrors.
   */
  public ErrorMessageDto(final String message, final String logRef,
      Map<String, String> fieldErrors) {
    this(message, logRef);
    this.errors = new ArrayList<>(fieldErrors.size());
    this.errors.addAll(fieldErrors.entrySet()
        .stream()
        .map(entry -> getErrorMessageDto(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList()));
  }

  /**
   * Error message block.
   *
   * @param path    path.
   * @param message message.
   * @return ErrorMessageDto.
   */
  public ErrorMessageDto getErrorMessageDto(final String path, final String message) {
    ErrorMessageDto dto = new ErrorMessageDto();
    dto.setPath(path);
    dto.setMessage(message);
    return dto;
  }

}
