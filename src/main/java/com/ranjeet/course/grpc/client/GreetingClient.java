package com.ranjeet.course.grpc.client;

import com.ranjeet.proto.calculator.CalculatorRequest;
import com.ranjeet.proto.calculator.CalculatorResponse;
import com.ranjeet.proto.calculator.CalculatorServiceGrpc;
import com.ranjeet.proto.calculator.PrimeNumberDecompositionRequest;
import com.ranjeet.proto.calculator.PrimeNumberDecompositionResponse;
import com.ranjeet.proto.greet.GreetManyRequest;
import com.ranjeet.proto.greet.GreetManyResponse;
import com.ranjeet.proto.greet.GreetRequest;
import com.ranjeet.proto.greet.GreetResponse;
import com.ranjeet.proto.greet.GreetServiceGrpc;
import com.ranjeet.proto.greet.Greeting;
import com.ranjeet.proto.greet.LongGreetRequest;
import com.ranjeet.proto.greet.LongGreetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
  public static void main(String[] args) {
    System.out.println("Hello gRPC client !!");
    System.out.println("Run");
    // run grpc client
    new GreetingClient().run();
  }

  public void run() {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5666)
        .usePlaintext()
        .build();

//    doUnaryCall(channel);
//    doServerStreamCall(channel);

    doClientStreamCall(channel);

    //shutdown channel
    System.out.println("Shutting down channel");
    channel.shutdown();
  }

  private void doClientStreamCall(ManagedChannel channel)  {
    GreetServiceGrpc.GreetServiceStub asyncGreetClient = GreetServiceGrpc.newStub(channel);
    final CountDownLatch latch = new CountDownLatch(1);
    StreamObserver<LongGreetRequest> requestStreamObserver = asyncGreetClient.longGreet(new StreamObserver<LongGreetResponse>() {
      @Override
      public void onNext(LongGreetResponse value) {
        System.out.println("Got server response");
        System.out.println(value.getResult());
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onCompleted() {
        // when response completed
        latch.countDown();
      }
    });
    System.out.println("sending request 1");
    requestStreamObserver.onNext(LongGreetRequest.newBuilder()
        .setGreeting(Greeting.newBuilder()
            .setFirstName("Ranjeet1")
            .build())
        .build());
    System.out.println("sending request 2");
    requestStreamObserver.onNext(LongGreetRequest.newBuilder()
        .setGreeting(Greeting.newBuilder()
            .setFirstName("Ranjeet2")
            .build())
        .build());
    System.out.println("sending request 3");
    requestStreamObserver.onNext(LongGreetRequest.newBuilder()
        .setGreeting(Greeting.newBuilder()
            .setFirstName("Ranjeet3")
            .build())
        .build());
    System.out.println("Done request");
    requestStreamObserver.onCompleted();
    
    // ....
    try {
      latch.await(3, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


  }


  private void doServerStreamCall(ManagedChannel channel) {
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
  }

  private void doUnaryCall(ManagedChannel channel) {
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

    // PrimeNumberDecomposition API

    PrimeNumberDecompositionRequest primeNumberDecompositionRequest = PrimeNumberDecompositionRequest.newBuilder()
        .setNumber(100000)
        .build();
    // calculation service
    CalculatorServiceGrpc.CalculatorServiceBlockingStub calculationSyncClient = CalculatorServiceGrpc.newBlockingStub(channel);
    Iterator<PrimeNumberDecompositionResponse> primeNumberDecompositionResponseIterator = calculationSyncClient.primeNumberDecomposition(primeNumberDecompositionRequest);

    System.out.println("PrimeNumberDecomposition API result for :" + primeNumberDecompositionRequest.getNumber());
    primeNumberDecompositionResponseIterator.forEachRemaining(v -> {
      System.out.println(v.getResult());
    });

  }

}
