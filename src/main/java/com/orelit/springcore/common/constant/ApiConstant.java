package com.orelit.springcore.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class for API constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstant {

    public static final String PREFIX = "/api";

    public static final String FORWARD_SLASH = "/";

    public static final String VERSION = "v1";

    public static final String BASE_PATH = PREFIX + FORWARD_SLASH + VERSION;
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";

    public static final String HTTP_PATCH = "PATCH";

    public static final String HTTP_DELETE = "DELETE";

    public static final String ALLOW_ALL = "/**";

    public static final String RESPONSE_CODE_400 = "400";

    public static final String RESPONSE_CODE_404 = "404";

    public static final String RESPONSE_CODE_500 = "500";

    public static final String BAD_REQUEST = "Bad request";

    public static final String NOT_FOUND = "Not found";

    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
}
