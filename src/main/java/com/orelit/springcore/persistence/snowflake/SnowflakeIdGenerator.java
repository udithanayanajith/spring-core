/*
 * Copyright (C) 2023 Sinorbis Technology or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Sinorbis Technology or its affiliates.
 */

package com.orelit.springcore.persistence.snowflake;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Snowflake id generator for create unique ids.
 */
public class SnowflakeIdGenerator implements IdentifierGenerator {

  private final Snowflake snowflake = new Snowflake();

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    return snowflake.nextId();
  }

}
