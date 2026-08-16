package example;

import io.zupix.Get;
import io.zupix.ZupixApp;
import io.zupix.ZupixApplication;

@ZupixApp
public class Application {
    @Get("/")
    public String hello() {
        return "Hello Zupix!";
    }

    public static void main(String[] args) throws Exception {
        try (var app = ZupixApplication.create(new Application(), 8080)) {
            app.start();
            System.out.println("Zupix running at http://localhost:" + app.port());
            Thread.currentThread().join();
        }
    }
}
