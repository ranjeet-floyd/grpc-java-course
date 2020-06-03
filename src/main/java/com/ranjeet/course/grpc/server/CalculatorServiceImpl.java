package com.ranjeet.course.grpc.server;

import com.ranjeet.proto.calculator.CalculatorRequest;
import com.ranjeet.proto.calculator.CalculatorResponse;
import com.ranjeet.proto.calculator.CalculatorServiceGrpc;
import com.ranjeet.proto.calculator.PrimeNumberDecompositionRequest;
import com.ranjeet.proto.calculator.PrimeNumberDecompositionResponse;
import com.ranjeet.proto.calculator.SquareRootRequest;
import com.ranjeet.proto.calculator.SquareRootResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
  @Override
  public void sum(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
    int a = request.getA();
    int b = request.getB();
    // calculate result
    int result = a + b;

    CalculatorResponse response = CalculatorResponse.newBuilder()
        .setResult(result)
        .build();

    // send result
    responseObserver.onNext(response);

    // mark complete
    responseObserver.onCompleted();

  }

  @Override
  public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
    int k = 2;
    int N = request.getNumber();
    while (N > 1) {
      if (N % k == 0) {   // if k evenly divides into N
        // 'k' is a factor
        System.out.println("Factor : " + k);
        responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
            .setResult(k)
            .build());
        N = N / k;  // divide N by k so that we have the rest of the number left.
      } else {
        k = k + 1;
      }
    }
    responseObserver.onCompleted();
  }

  @Override
  public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
    int number = request.getNumber();
    if(number > 0 ) {
      double result = Math.sqrt(number);
      responseObserver.onNext(SquareRootResponse.newBuilder()
          .setResult(result)
          .build());
      responseObserver.onCompleted();
    }
    else {
      responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Input request number must be positive")
          .augmentDescription("Request number is : "+ number)
          .asRuntimeException());
    }
  }
}
