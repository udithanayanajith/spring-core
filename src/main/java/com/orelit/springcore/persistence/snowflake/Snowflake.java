/*
 * Copyright (C) 2023 Sinorbis Technology or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Sinorbis Technology or its affiliates.
 */

package com.orelit.springcore.persistence.snowflake;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

/**
 * Distributed Sequence Generator. Inspired by Twitter snowflake:
 * https://github.com/twitter/snowflake/tree/snowflake-2010.
 * This class should be used as a Singleton. Make sure that you create and reuse a Single instance
 * of Snowflake per node in your distributed system cluster.
 * reference
 * https://github.com/callicoder/java-snowflake/blob/master/src/main/java/com/callicoder/snowflake/Snowflake.java
 */
public class Snowflake {

  private static final int EPOCH_BITS = 41;
  private static final int NODE_ID_BITS = 10;
  private static final int SEQUENCE_BITS = 12;

  private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
  private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

  //Custom Epoc = 00:00:00 01/01/2011. NEVER CHANGE THIS
  private static final long DEFAULT_CUSTOM_EPOCH = 1293840000000L;

  private final long node;
  private final long customEpoch;

  private volatile long lastTimestamp = -1L;
  private volatile long sequence = 0L;

  /**
   * Create Snowflake with a nodeId and custom epoch.
   *
   * @param node        node
   * @param customEpoch customEpoch
   */
  public Snowflake(long node, long customEpoch) {
    if (node < 0 || node > MAX_NODE_ID) {
      throw new IllegalArgumentException(
          String.format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
    }
    this.node = node;
    this.customEpoch = customEpoch;
  }

  /**
   * Create Snowflake with a nodeId.
   *
   * @param node nodeId
   */
  public Snowflake(long node) {
    this(node, DEFAULT_CUSTOM_EPOCH);
  }

  /**
   * Let Snowflake generate a nodeId.
   */
  public Snowflake() {
    this.node = createNodeId();
    this.customEpoch = DEFAULT_CUSTOM_EPOCH;
  }

  /**
   * Get the next id.
   *
   * @return long id.
   */
  public synchronized long nextId() {
    long currentTimestamp = timestamp();

    if (currentTimestamp < lastTimestamp) {
      throw new IllegalStateException("Invalid System Clock!");
    }

    if (currentTimestamp == lastTimestamp) {
      sequence = (sequence + 1) & MAX_SEQUENCE;
      if (sequence == 0) {
        // Sequence Exhausted, wait till next millisecond.
        currentTimestamp = waitNextMillis(currentTimestamp);
      }
    } else {
      // reset sequence to start with zero for the next millisecond
      sequence = 0;
    }

    lastTimestamp = currentTimestamp;

    return currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS)
        | (node << SEQUENCE_BITS)
        | sequence;
  }


  /**
   * Get current timestamp in milliseconds, adjust for the custom epoch.
   *
   * @return Current timestamp in milliseconds
   */
  private long timestamp() {
    return Instant.now().toEpochMilli() - customEpoch;
  }

  /**
   * Block and wait till next millisecond.
   *
   * @param currentTimestamp currentTimestamp
   * @return currentTimestamp
   */
  private long waitNextMillis(long currentTimestamp) {
    while (currentTimestamp == lastTimestamp) {
      currentTimestamp = timestamp();
    }
    return currentTimestamp;
  }

  /**
   * Creates a node id.
   *
   * @return Node id.
   */
  private long createNodeId() {
    long nodeId;
    try {
      StringBuilder sb = new StringBuilder();
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        byte[] mac = networkInterface.getHardwareAddress();
        if (mac != null) {
          for (byte macPort : mac) {
            sb.append(String.format("%02X", macPort));
          }
        }
      }
      nodeId = sb.toString().hashCode();
    } catch (Exception ex) {
      nodeId = (new SecureRandom().nextInt());
    }
    nodeId = nodeId & MAX_NODE_ID;
    return nodeId;
  }

  @Override
  public String toString() {
    return "Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS
        + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch
        + ", NodeId=" + node + "]";
  }
}

