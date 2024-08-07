package com.quaxt;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class JShellServer {

  private static void processEvent(SnippetEvent event, PrintWriter out) {
    if (event.status() == Snippet.Status.VALID) {
      out.println(event.value());
    } else {
      out.println("Error: " + event.status());
    }
    out.flush();
  }

  public static void main(String[] args) {
    startNewServer(new ArgsParser(args).getPort());
  }

  public static void startNewServer(int port) {
    try (ServerSocket serverSocket = new ServerSocket(port);
         JShell jshell = JShell.create()) {
      while (true) {
        System.out.println("Server started, waiting for connection...");

        try {
          Socket clientSocket = serverSocket.accept();
          System.out.println("Client connected.");

          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            jshell.eval(inputLine).forEach(event -> processEvent(event, out));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
