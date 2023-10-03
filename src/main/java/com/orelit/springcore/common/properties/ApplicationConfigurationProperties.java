package com.orelit.springcore.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application specific configuration properties.
 */
@Getter
@Setter
@ConfigurationProperties("application")
public class ApplicationConfigurationProperties {

    private String display;

    private String timeZone;
}
