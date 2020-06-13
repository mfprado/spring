package com.mfprado.server.http;

import java.util.Map;

public class URL {
  private Protocol protocol;
  private String host;
  private String path;
  private Map<String, String> queryParams;

  public URL(Protocol protocol, String host, String path, Map<String, String> queryParams) {
    this.protocol = protocol;
    this.host = host;
    this.path = path;
    this.queryParams = queryParams;
  }
}
