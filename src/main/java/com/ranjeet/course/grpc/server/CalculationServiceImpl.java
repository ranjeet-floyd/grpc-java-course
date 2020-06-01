package com.ranjeet.course.grpc.server;

import com.ranjeet.proto.calculation.CalculationRequest;
import com.ranjeet.proto.calculation.CalculationResponse;
import com.ranjeet.proto.calculation.CalculationServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CalculationServiceImpl extends CalculationServiceGrpc.CalculationServiceImplBase {
  @Override
  public void sum(CalculationRequest request, StreamObserver<CalculationResponse> responseObserver) {
    int a = request.getA();
    int b = request.getB();
    // calculate result
    int result = a + b ;

    CalculationResponse response = CalculationResponse.newBuilder()
        .setResult(result)
        .build();
    
    // send result
    responseObserver.onNext(response);

    // mark complete
    responseObserver.onCompleted();

  }
}
