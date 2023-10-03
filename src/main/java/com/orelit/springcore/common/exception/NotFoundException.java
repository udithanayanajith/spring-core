package com.orelit.springcore.common.exception;

/**
 * An exception that indicates a requested resource was not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {super(message);}

    public NotFoundException(final String message, final Throwable cause){super(message,cause);}

    /**
     * Creates {@link NotFoundException}
     *
     * @param type - The type of resource, like 'Country'
     * @param id - ID of the resource, like '123'
     * @return Exception with message: "No {type} found with id: '{id}'"
     */
    public static NotFoundException fromResources(String type, Object id) {
        return new NotFoundException("No %s found with id: '%s'".formatted(type, id));
    }

    /**
     * Creates {@link NotFoundException}
     *
     * @param type - The type of resource, like 'Country'
     * @param idType - Type of ID used, like 'code'
     * @param id - ID of the resource, like '123'
     * @return Exception with message: "No {type} found with {idType}: '{id}'"
     */
    public static NotFoundException fromResources(String type, String idType, Object id) {
        return new NotFoundException("No %s found with %s: '%s'".formatted(type, idType, id));
    }

    /**
     * Creates {@link NotFoundException}
     *
     * @param object - Generic object, like 'Contact'
     * @return Exception with message: "{object} not found"
     */
    public static NotFoundException fromResources(String object) {
        return new NotFoundException("%s not found".formatted(object));
    }
}
