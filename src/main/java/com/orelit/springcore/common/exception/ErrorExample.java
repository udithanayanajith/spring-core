package com.orelit.springcore.common.exception;

/**
 * Examples of error messages that will be returned by the API. Reference this
 * in your Controller classes in an {@code @ExampleObject} inside and {@code @ApiResponse}.
 *
 * <p> Example:
 * <pre>{@code
 *      @ResponseStatus(code = HttpStatus.CREATED)
 *      @ApiResponse(responseCode = "201", description = "Created")
 *      @ApiResponse(responseCode = "400", description = "Validation failed",
 *              content = @Content(schema = @schema(implementation = ErrorMessageDto.class),
 *              examples = @ExampleObject(ErrorExample.VALIDATION_FAILED)))
 *      public ObjectWeb createObject(ObjectWeb obj) { ... }
 * }</pre>
 */
public class ErrorExample {
    private ErrorExample() {}

    public static final String VALIDATION_FAILED = """
            {
                "message": "Validation failed",
                "logref": "ERROR1DEYR5DTE4R",
                "type": "PropertyValidationException",
                "errors": [
                    {
                        "path": "name",
                        "message": "is required"
                    }
                ]
            }
            """;

    public static final String NOT_FOUND = """ 
            {
                "message": "Resource not found",
                "logref": "ERROR1DEYR5DTE4R",
                "type": "NotFoundException"
            }
            """;

    public static final String OPTIMISTIC_LOCK = """
            {
                "message": "Object of class [com.sinorbis.bifrost.api.entity.{entity}] with identifier [objectId]: optimistic locking failed",
                "logref": "ERROR1DEYR5DTE4R",
                "type": "OptimisticLockingFailureException"
            }
            """;
}
