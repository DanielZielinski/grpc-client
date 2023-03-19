package daniel.zielinski.grpcclient.clientstreaming;

import com.google.protobuf.ByteString;
import daniel.zielinski.grpc.File;
import daniel.zielinski.grpc.FileServiceGrpc;
import daniel.zielinski.grpc.FileUploadRequest;
import daniel.zielinski.grpc.FileUploadResponse;
import daniel.zielinski.grpc.MetaData;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileClientImpl implements FileClient {

    @GrpcClient("grpc-server")
    private FileServiceGrpc.FileServiceStub fileServiceStub;

    private final StreamObserver<FileUploadResponse> fileUploadObserver;

    @Override
    public void sendFile(String filePath) throws IOException {

        StreamObserver<FileUploadRequest> streamObserver = fileServiceStub.upload(fileUploadObserver);
        Path path = Paths.get(filePath);

        FileUploadRequest metadata = FileUploadRequest.newBuilder()
                .setMetadata(MetaData.newBuilder()
                        .setName("outputFile")
                        .setType(FilenameUtils.getExtension(filePath)).build())
                .build();
        streamObserver.onNext(metadata);

        try (InputStream inputStream = Files.newInputStream(path)) {
            byte[] bytes = new byte[4096];
            int size;
            while ((size = inputStream.read(bytes)) > 0) {
                FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                        .setFile(File.newBuilder().setContent(ByteString.copyFrom(bytes, 0, size)).build())
                        .build();
                streamObserver.onNext(uploadRequest);
            }
        }

        streamObserver.onCompleted();
    }

}