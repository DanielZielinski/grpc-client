package daniel.zielinski.grpcclient.clientstreaming;

import daniel.zielinski.grpc.FileUploadResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadObserver implements StreamObserver<FileUploadResponse> {

    @Override
    public void onNext(FileUploadResponse fileUploadResponse) {
        log.info("Status {}", fileUploadResponse.getStatus());
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurs while uploading file", throwable);
    }

    @Override
    public void onCompleted() {
        log.info("File uploaded successfully");
    }

}
