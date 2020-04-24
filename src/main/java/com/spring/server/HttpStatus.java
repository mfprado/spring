package com.spring.server;

public enum HttpStatus {
  OK(200, "OK"),
  NO_CONTENT(204, "No Content"),
  BAD_REQUEST(400, "Bad Request"),
  NOT_FOUND(404, "Not Found"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private int code;
  private String description;

  HttpStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
