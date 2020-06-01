package com.ranjeet.course.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GreetingServer {
  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello gRPC !!");

    Server server = ServerBuilder.forPort(5666)
        .build();
    server.start();
    
    // if Java Runtime Exception
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Received server shutdown request");
      server.shutdown();
      System.out.println("Successfully stopped server");
    }));
    
    // keep running server
    server.awaitTermination();
    
    
  }
}
