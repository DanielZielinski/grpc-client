package daniel.zielinski.grpcclient;

import daniel.zielinski.grpc.HelloGrpc;
import daniel.zielinski.grpc.HelloReply;
import daniel.zielinski.grpc.HelloRequest;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrpcClientService {

    @GrpcClient("grpc-server")
    private HelloGrpc.HelloBlockingStub simpleStub;

    public String sendMessage(final String name) {
        try {
            log.info("sending request");
            final HelloReply response = this.simpleStub.sayHello(HelloRequest.newBuilder().setName(name).build());
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
    }

}