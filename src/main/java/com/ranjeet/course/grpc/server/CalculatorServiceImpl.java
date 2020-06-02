package com.ranjeet.course.grpc.server;

import com.ranjeet.proto.calculator.CalculatorRequest;
import com.ranjeet.proto.calculator.CalculatorResponse;
import com.ranjeet.proto.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
  @Override
  public void sum(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
    int a = request.getA();
    int b = request.getB();
    // calculate result
    int result = a + b ;

    CalculatorResponse response = CalculatorResponse.newBuilder()
        .setResult(result)
        .build();
    
    // send result
    responseObserver.onNext(response);

    // mark complete
    responseObserver.onCompleted();

  }
}
