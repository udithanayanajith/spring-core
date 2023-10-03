package com.orelit.springcore.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * Utility class to get the value for known MDC keys.
 */

@Slf4j
public class MdcUtils {

    public static final String USER_NAME_KEY = "user";

    private MdcUtils(){

    }

    /**
     * Get the current user's display name, for instance "John Smith".
     *
     * @return user display name, if provided. Can be null or empty.
     */
    public static String getUserDisplayName () {
        return MDC.get(USER_NAME_KEY);
    }
}
