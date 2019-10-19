package de.d3adspace.skylla.protocol.exception;

public class ProtocolException extends Exception {

  protected ProtocolException(String message) {
    super(message);
  }

  protected ProtocolException(String message, Throwable cause) {
    super(message, cause);
  }
}
