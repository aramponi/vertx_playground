package net.ramponi;

import net.ramponi.mpp.ProcessURLRequestHandler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.ReplyException;
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
                final String url = params.contains("url") ?  params.get("url") : "DefaultUrl";

                vertx.eventBus().sendWithTimeout(MPP_MESSAGE_HANDLER, url, 500, new Handler<AsyncResult<Message<String>>>() {

                    public void handle(AsyncResult<Message<String>> result) {
                        if (result.succeeded()) {
                            req.response().setStatusCode(200);
                            req.response().putHeader("Content-Type", "application/json");
                            req.response().end(result.result().body());
                        }  else {
                            ReplyException ex = (ReplyException)result.cause();
                            System.err.println("Failure type: " + ex.failureType());
                            System.err.println("Failure code: " + ex.failureCode());
                            System.err.println("Failure message: " + ex.getMessage());
                            vertx.eventBus().sendWithTimeout(MPP_MESSAGE_HANDLER, url, 500, this);
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


        vertx.createHttpServer().requestHandler(matcher).listen(7890);


        container.logger().info("Webserver started, listening on port: 8888");

    }
}
