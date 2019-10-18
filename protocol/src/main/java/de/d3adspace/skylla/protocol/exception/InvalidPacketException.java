package de.d3adspace.skylla.protocol.exception;

import com.google.common.base.Preconditions;

public class InvalidPacketException extends ProtocolException {

  private InvalidPacketException(String message) {
    super(message);
  }

  public InvalidPacketException(String message, Throwable cause) {
    super(message, cause);
  }

  public static InvalidPacketException withMessage(String message) {
    Preconditions.checkNotNull(message);

    return new InvalidPacketException(message);
  }

  public static InvalidPacketException withMessageAndCause(String message, Throwable cause) {
    Preconditions.checkNotNull(message);
    Preconditions.checkNotNull(cause);

    return new InvalidPacketException(message, cause);
  }
}
