package de.d3adspace.skylla.protocol.exception;

import com.google.common.base.Preconditions;

public final class InvalidPacketException extends ProtocolException {

  private InvalidPacketException(String message) {
    super(message);
  }

  public InvalidPacketException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create an exception instance with the given message.
   *
   * @param message The message.
   * @return The exception.
   */
  public static InvalidPacketException withMessage(String message) {
    Preconditions.checkNotNull(message);

    return new InvalidPacketException(message);
  }

  /**
   * Create an exception instance with the given message that is caused by the given cause.
   *
   * @param message The message.
   * @param cause   The cause.
   * @return The exception.
   */
  public static InvalidPacketException withMessageAndCause(String message, Throwable cause) {
    Preconditions.checkNotNull(message);
    Preconditions.checkNotNull(cause);

    return new InvalidPacketException(message, cause);
  }
}
