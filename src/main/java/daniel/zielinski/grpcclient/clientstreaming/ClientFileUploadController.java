package daniel.zielinski.grpcclient.clientstreaming;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/upload-file")
@RequiredArgsConstructor
public class ClientFileUploadController {

    private final GrpcStreamingClientService grpcStreamingClientService;

    @PostMapping
    public ResponseEntity<Void> uploadFile(@RequestParam String filePath) throws IOException {
        grpcStreamingClientService.sendFile(filePath);
        return ResponseEntity.ok().build();
    }
}
