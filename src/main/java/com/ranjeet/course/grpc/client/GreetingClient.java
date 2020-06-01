package com.ranjeet.course.grpc.client;

import com.proto.dummy.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
  public static void main(String[] args) {
    System.out.println("Hello gRPC client !!");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5666)
        .usePlaintext()
        .build();

    System.out.println("Creating stub");
    // sync client
    DummyServiceGrpc.DummyServiceBlockingStub sync = DummyServiceGrpc.newBlockingStub(channel);
    //async client
//    DummyServiceGrpc.DummyServiceFutureStub async = DummyServiceGrpc.newFutureStub(channel);
    
    // call service method
//    sync.
    
    //shutdown channel
    System.out.println("Shutting down channel");
    channel.shutdown();
    
  }
}
