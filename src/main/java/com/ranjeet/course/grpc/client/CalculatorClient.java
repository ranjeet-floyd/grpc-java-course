package com.ranjeet.course.grpc.client;

import com.ranjeet.proto.calculator.CalculatorServiceGrpc;
import com.ranjeet.proto.calculator.SquareRootRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class CalculatorClient {


  public void run() {
    System.out.println("run grpc client");
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5666)
        .usePlaintext()
        .build();
  
    // Exception handling
    
    doExeptionHandleCall(channel);


    //shutdown channel
    System.out.println("Shutting down channel");
    channel.shutdown();
  }

  private void doExeptionHandleCall(ManagedChannel channel) {
    CalculatorServiceGrpc.CalculatorServiceBlockingStub syncClient = CalculatorServiceGrpc.newBlockingStub(channel);
    int requestNumber = -1;
    try {
      syncClient.squareRoot(SquareRootRequest.newBuilder()
          .setNumber(requestNumber)
          .build());

    }
    catch (StatusRuntimeException ex){
      System.err.println("Got error from server");
      ex.printStackTrace();
    }
   

  }

  public static void main(String[] args) {
    new CalculatorClient().run();
  }
}
