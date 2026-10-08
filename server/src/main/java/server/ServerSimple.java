package server;
import io.javalin.Javalin;

public class ServerSimple {
    public static void main(String[] args) {
        Javalin.create(config->config.staticFiles.add("public"))
                .get("/hello", ctx->ctx.result("Hello!"))
                .post("/name/{name}", (name)->System.out.println(name.fullUrl()))
                .start(8080);
    }
}
