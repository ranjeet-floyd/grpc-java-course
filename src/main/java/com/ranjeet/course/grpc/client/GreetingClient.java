package com.ranjeet.course.grpc.client;

import com.ranjeet.proto.calculator.CalculatorRequest;
import com.ranjeet.proto.calculator.CalculatorResponse;
import com.ranjeet.proto.calculator.CalculatorServiceGrpc;
import com.ranjeet.proto.greet.GreetManyRequest;
import com.ranjeet.proto.greet.GreetManyResponse;
import com.ranjeet.proto.greet.GreetRequest;
import com.ranjeet.proto.greet.GreetResponse;
import com.ranjeet.proto.greet.GreetServiceGrpc;
import com.ranjeet.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Iterator;

public class GreetingClient {
  public static void main(String[] args) {
    System.out.println("Hello gRPC client !!");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5666)
        .usePlaintext()
        .build();

    System.out.println("Creating stub");
    // sync client
//    DummyServiceGrpc.DummyServiceBlockingStub sync = DummyServiceGrpc.newBlockingStub(channel);
    //async client
//    DummyServiceGrpc.DummyServiceFutureStub async = DummyServiceGrpc.newFutureStub(channel);

    GreetServiceGrpc.GreetServiceBlockingStub syncGreetClient = GreetServiceGrpc.newBlockingStub(channel);

    // build greet request
    Greeting greeting = Greeting.newBuilder()
        .setFirstName("Ranjeet")
        .setLastName("Kumar")
        .build();
    GreetRequest greetRequest = GreetRequest.newBuilder()
        .setGreeting(greeting)
        .build();

    // call greet service method
    GreetResponse greetResponse = syncGreetClient.greet(greetRequest);

    // print response
    System.out.println(greetResponse);
    
    // calculation service
    CalculatorServiceGrpc.CalculatorServiceBlockingStub calculationSyncClient =
        CalculatorServiceGrpc.newBlockingStub(channel);

    CalculatorRequest request = CalculatorRequest.newBuilder()
        .setA(5)
        .setB(6)
        .build();

    CalculatorResponse sum = calculationSyncClient.sum(request);

    // print sum result
    System.out.println(sum);

    // client for server stream
    GreetServiceGrpc.GreetServiceBlockingStub clientSyncServerStream = GreetServiceGrpc.newBlockingStub(channel);
    GreetManyRequest greetManyRequest = GreetManyRequest.newBuilder()
        .setGreeting(Greeting.newBuilder()
            .setFirstName("Ranjeet")
            .setLastName("Kumar").build())
        .build();
    Iterator<GreetManyResponse> greetManyResponseIterator = clientSyncServerStream.greetManyTimes(greetManyRequest);

    while (greetManyResponseIterator.hasNext()) {
      System.out.println(greetManyResponseIterator.next());
    }


    //shutdown channel
    System.out.println("Shutting down channel");
    channel.shutdown();

  }
}
