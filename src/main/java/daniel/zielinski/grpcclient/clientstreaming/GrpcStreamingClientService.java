package daniel.zielinski.grpcclient.clientstreaming;

import com.google.protobuf.ByteString;
import daniel.zielinski.grpc.File;
import daniel.zielinski.grpc.FileServiceGrpc;
import daniel.zielinski.grpc.FileUploadRequest;
import daniel.zielinski.grpc.MetaData;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class GrpcStreamingClientService {

    @GrpcClient("grpc-server")
    private FileServiceGrpc.FileServiceStub fileServiceStub;


    public void sendFile(String filePath) throws IOException {

        StreamObserver<FileUploadRequest> streamObserver = fileServiceStub.upload(new FileUploadObserver());
        Path path = Paths.get(filePath);

        FileUploadRequest metadata = FileUploadRequest.newBuilder()
                .setMetadata(MetaData.newBuilder()
                        .setName("outputFile")
                        .setType("png").build())
                .build();
        streamObserver.onNext(metadata);

        java.io.File file = new java.io.File(filePath);

        try (InputStream inputStream = Files.newInputStream(path)) {
            byte[] bytes = new byte[4096];
            int size;
            int processed = 0;
            while ((size = inputStream.read(bytes)) > 0) {
                FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                        .setFile(File.newBuilder().setContent(ByteString.copyFrom(bytes, 0, size)).build())
                        .build();
                streamObserver.onNext(uploadRequest);
                processed += size;
                logProgress(processed, file.length());
            }
        }

        streamObserver.onCompleted();
    }

    private void logProgress(int processed, long total) {
        log.info("Progress {}", ((double) processed / (double) total) * 100.00d);
    }

}