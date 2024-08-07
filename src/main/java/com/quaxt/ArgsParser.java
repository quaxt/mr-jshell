package com.quaxt;
import java.util.Arrays;
import java.util.List;

public class ArgsParser {
  private int port = 12345;

  public ArgsParser(String[] args) {
    List<String> argsList = Arrays.asList(args);
    for (int i = 0; i < argsList.size(); i++) {
      String arg = argsList.get(i);
      if ("--port".equals(arg)) {
        setPort(Integer.parseInt(argsList.get(i+1)));
      }
    }
  }

  private void setPort(int i) {
    port = i;
  }

  public int getPort() {
    return port;
  }
}
