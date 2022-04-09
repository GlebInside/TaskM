

import service.*;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {


        new KVServer().start();

        var manager = Managers.getDefault();
        var server = new HttpTaskServer((InMemoryTaskManager) manager);
    }
}
