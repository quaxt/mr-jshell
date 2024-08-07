package com.quaxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class JShellClient {
  static AtomicBoolean exiting = new AtomicBoolean(false);

  public static void startNewClient(int port) {
    try (Socket socket = new Socket("localhost", port);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
      Thread t = new Thread(() -> {
         while (!exiting.get()) {
          String response = null;
          try {
            response = in.readLine();
          } catch (IOException e) {
            e.printStackTrace();
          }
          System.out.println("Server: " + response);
        }
      });
      t.start();
      String userInput;
      while ((userInput = stdIn.readLine()) != null) {
        if (userInput.equals("/exit")) {
          exiting.set(true);
          return;
        }
        out.println(userInput);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    startNewClient(new ArgsParser(args).getPort());
  }
}