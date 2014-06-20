package net.ramponi;

import net.ramponi.mpp.ProcessURLRequestHandler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import static net.ramponi.Constants.*;
/**
 * Created with IntelliJ IDEA.
 * User: tony
 * Date: 19/06/2014
 * Time: 08:46
 * Zeez v1
 */
public class MppVerticle extends Verticle {


    public void start() {
        JsonObject config = container.config();

        System.out.println("Config is " + config);

        EventBus eb = vertx.eventBus();
        // init Message Handlers
        ProcessURLRequestHandler processURLRequestHandler = new ProcessURLRequestHandler(eb);

        eb.registerHandler(MPP_MESSAGE_HANDLER, new ProcessURLRequestHandler(eb));

        RouteMatcher matcher = new RouteMatcher();

        // the matcher for the complete list and the search
        matcher.get("/r1", new Handler<HttpServerRequest>() {

            @Override
            public void handle(final HttpServerRequest req) {
                MultiMap params = req.params();
                String url = "DefaultUrl";
                if (params.contains("url")) {
                    url = params.get("url");
                }
                vertx.eventBus().sendWithTimeout(MPP_MESSAGE_HANDLER, url, 60000, new Handler<AsyncResult<Message<String>>>() {

                    public void handle(AsyncResult<Message<String>> result) {
                        if (result.succeeded()) {
                            req.response().setStatusCode(200);
                            req.response().putHeader("Content-Type", "application/json");
                            req.response().end(result.result().body());
                        }
                    }

                });
            }
        });

        // the matcher for a specific id
        matcher.post("/pset/:id", new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {


            }
        });


        vertx.createHttpServer().requestHandler(matcher).listen(8888);


        container.logger().info("Webserver started, listening on port: 8888");

    }
}
