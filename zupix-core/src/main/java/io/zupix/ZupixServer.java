package io.zupix;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;

/** Minimal HTTP runtime for the Zupix foundation. */
public final class ZupixServer implements AutoCloseable {
    private final HttpServer server; private final Router router; private final MiddlewareRegistry middleware; private final ExceptionRegistry exceptions;
    private ZupixServer(HttpServer server, Router router, MiddlewareRegistry middleware, ExceptionRegistry exceptions) { this.server=server; this.router=router; this.middleware=middleware; this.exceptions=exceptions; }
    public static ZupixServer create(int port, Router router) throws IOException { return create(port, router, new MiddlewareRegistry(), new ExceptionRegistry()); }
    public static ZupixServer create(int port, Router router, MiddlewareRegistry middleware, ExceptionRegistry exceptions) throws IOException {
        Objects.requireNonNull(router,"router"); HttpServer server=HttpServer.create(new InetSocketAddress(port),0); ZupixServer runtime=new ZupixServer(server,router,middleware,exceptions); server.createContext("/",runtime::handle); server.setExecutor(Executors.newVirtualThreadPerTaskExecutor()); return runtime;
    }
    public void start(){server.start();} public int port(){return server.getAddress().getPort();} @Override public void close(){server.stop(0);}
    private void handle(HttpExchange exchange) throws IOException {
        String path=exchange.getRequestURI().getPath();
        String body=new String(exchange.getRequestBody().readAllBytes(),StandardCharsets.UTF_8);
        RequestContext request=new RequestContext(exchange.getRequestMethod(),path,exchange.getRequestURI().getRawQuery(),body); HttpResponse state=new HttpResponse();
        try { new MiddlewarePipeline(middleware.all()).execute(request,()->{try{state.set(dispatch(exchange,body));}catch(RuntimeException e){Response handled=exceptions.handle(e);if(handled!=null)state.set(handled);else throw e;}}); }
        catch(RuntimeException e){Response handled=exceptions.handle(e);state.set(handled!=null?handled:Response.status(500,"Internal Server Error"));}
        writeResponse(exchange,state.get()==null?Response.status(500,"No response produced"):state.get());
    }
    private Response dispatch(HttpExchange exchange,String body){
        Router.MatchedRoute matched=router.match(exchange.getRequestMethod(),exchange.getRequestURI().getPath()); if(matched==null)return Response.status(404,"Not Found");
        try{Object result=matched.route().handler().invoke(matched.parameters(),exchange.getRequestURI().getRawQuery(),body);return result instanceof Response r?r:Response.ok(result);}
        catch(ForbiddenException e){return Response.status(403,e.getMessage());}
        catch(ValidationException e){return Response.status(422,java.util.Map.of("detail",e.errors()));}
        catch(IllegalArgumentException e){return Response.status(400,e.getMessage());}
    }
    private static void writeResponse(HttpExchange exchange,Response result)throws IOException{applyHeaders(exchange,result.headers());Object body=result.body();String type;byte[] bytes;if(body==null){bytes=new byte[0];type="text/plain; charset=utf-8";}else if(body instanceof String text){bytes=text.getBytes(StandardCharsets.UTF_8);type="text/plain; charset=utf-8";}else{bytes=Json.write(body).getBytes(StandardCharsets.UTF_8);type="application/json; charset=utf-8";}if(exchange.getResponseHeaders().getFirst("Content-Type")==null)exchange.getResponseHeaders().set("Content-Type",type);exchange.sendResponseHeaders(result.status(),bytes.length);try(OutputStream output=exchange.getResponseBody()){output.write(bytes);}}
    private static void applyHeaders(HttpExchange exchange,java.util.Map<String,String> headers){headers.forEach((name,value)->exchange.getResponseHeaders().set(name,value));}
}
