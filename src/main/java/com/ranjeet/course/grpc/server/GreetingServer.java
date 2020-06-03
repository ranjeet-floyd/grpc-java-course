package com.ranjeet.course.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.File;
import java.io.IOException;

public class GreetingServer {
  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello gRPC !!");

    // plain text server
//    Server server = ServerBuilder.forPort(5666)
//        .addService(new GreetServiceImpl())
//        .addService(new CalculatorServiceImpl())
//        .build(); 
    Server server = ServerBuilder.forPort(5666)
        .useTransportSecurity(
            new File("ssl/server.crt"),
            new File("ssl/server.pem")
        )
        .addService(new GreetServiceImpl())
        .addService(new CalculatorServiceImpl())
        .build();
    server.start();
    
    // if Java Runtime Exception
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Received server shutdown request");
      server.shutdown();
      System.out.println("Successfully stopped server");
    }));
    System.out.println("gRPC server started...");
    // keep running server
    server.awaitTermination();
    
  }
}
