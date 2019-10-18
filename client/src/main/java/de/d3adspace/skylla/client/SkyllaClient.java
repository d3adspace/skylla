package de.d3adspace.skylla.client;

public interface SkyllaClient {

  void connect();

  boolean isConnected();

  void disconnect();
}
