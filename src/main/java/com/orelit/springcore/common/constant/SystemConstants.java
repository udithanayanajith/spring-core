package com.orelit.springcore.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Common constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemConstants {

    public static final String EMAIL_REGEX = "^\\s*[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}\\s*$";

    public static final String PHONE_NUMBER_REGEX = "^\\s*\\+(?:[0-9]?){6,10}[0-9]\\s*$";

    public static final String NAME_REGEX = "^[^-0-9_!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:[\\\\]]{2,}$";

    public static final String NUMBER_REGEX = "^[-+]?[0-9]*\\.?[0-9]+$";
}
