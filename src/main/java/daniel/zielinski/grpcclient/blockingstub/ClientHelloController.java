package daniel.zielinski.grpcclient.blockingstub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientHelloController {

    private final GrpcBlockingStubClientService grpcBlockingStubClientService;

    @PostMapping("/")
    public String printMessage(@RequestParam(defaultValue = "hi") String msg) {
        return grpcBlockingStubClientService.sendMessage(msg);
    }
}
