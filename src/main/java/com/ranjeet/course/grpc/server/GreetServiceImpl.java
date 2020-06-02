package com.ranjeet.course.grpc.server;

import com.ranjeet.proto.greet.GreetManyRequest;
import com.ranjeet.proto.greet.GreetManyResponse;
import com.ranjeet.proto.greet.GreetRequest;
import com.ranjeet.proto.greet.GreetResponse;
import com.ranjeet.proto.greet.GreetServiceGrpc;
import com.ranjeet.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

  @Override
  public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
    
    // extract field from request
    Greeting requestGreeting = request.getGreeting();
    String firstName = requestGreeting.getFirstName();
    String lastName = requestGreeting.getLastName();
    
    // build result
    String result = "Hello " + firstName + " " + lastName;

    // build response
    GreetResponse response = GreetResponse.newBuilder()
        .setResult(result)
        .build();

    // send result
    responseObserver.onNext(response);

    // mark as complete
    responseObserver.onCompleted();
  }

  @Override
  public void greetManyTimes(GreetManyRequest request, StreamObserver<GreetManyResponse> responseObserver) {
    Greeting requestGreeting = request.getGreeting();

    String firstName = requestGreeting.getFirstName();
    String lastName = requestGreeting.getLastName();
    try {
      for (int i = 0; i < 10; i++) {
        String result = "Many Greeting to " + firstName + " " + lastName + " sending " + i;
        GreetManyResponse greetResponse = GreetManyResponse.newBuilder()
            .setResult(result)
            .build();
        responseObserver.onNext(greetResponse);
        Thread.sleep(1000);// sleep for 1 sec ..show it is actually streaming result
      }
    } catch (InterruptedException e) {
      responseObserver.onError(e);
    } finally {
      responseObserver.onCompleted();
    }


  }
}
