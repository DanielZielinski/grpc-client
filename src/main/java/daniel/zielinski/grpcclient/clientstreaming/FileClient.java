package daniel.zielinski.grpcclient.clientstreaming;

import java.io.IOException;

public interface FileClient {

     void sendFile(String filePath) throws IOException;

}