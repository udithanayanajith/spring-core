/*
 * Copyright (C) 2023 Sinorbis Technology or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Sinorbis Technology or its affiliates.
 */

package com.orelit.springcore.common.util;


import com.orelit.springcore.common.properties.ApplicationConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Class to retrieve utility information.
 */
@Configuration
@EnableConfigurationProperties(ApplicationConfigurationProperties.class)
public class SystemUtils {

  private static ApplicationConfigurationProperties properties;

  public SystemUtils(ApplicationConfigurationProperties properties) {
    SystemUtils.properties = properties;
  }

  /**
   * Retrieve local date & time according to local time zone.
   *
   * @return - Local Date & Time
   */
  public static LocalDateTime currentDateTime() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }

  /**
   * Date format foe CSV file.
   *
   * @return String value- data format.
   */
  public static String dateFormatForCsv() {
    return "dd MM yyyy";
  }

  /**
   * LocalDate convert to string.
   *
   * @param localDateTime - LocalDate
   * @return String value.
   */
  public static String localDateConvertToString(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern(dateFormatForCsv()));
  }

  /**
   * Retrieve local date according to local time zone.
   *
   * @return - Local Date
   */
  public static LocalDate currentDate() {
    return LocalDate.now(ZoneId.of(properties.getTimeZone()));
  }


  /**
   * Retrieve local time according to local time zone.
   *
   * @return - Local time
   */
  public static LocalTime currentTime() {
    return LocalTime.now(ZoneId.of(properties.getTimeZone()));
  }


}
