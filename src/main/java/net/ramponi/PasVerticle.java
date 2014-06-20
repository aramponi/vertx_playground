package net.ramponi;

import net.ramponi.mpp.ProcessURLRequestHandler;
import net.ramponi.pas.ProcessPSetRequestHandler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import static net.ramponi.Constants.MPP_MESSAGE_HANDLER;
import static net.ramponi.Constants.PAS_MESSAGE_HANDLER;

/**
 * Created with IntelliJ IDEA.
 * User: tony
 * Date: 19/06/2014
 * Time: 08:46
 * Zeez v1
 */
public class PasVerticle extends Verticle {


    public void start() {
        JsonObject config = container.config();

        System.out.println("Config is " + config);

        EventBus eb = vertx.eventBus();
        // init Message Handlers

        eb.registerHandler(PAS_MESSAGE_HANDLER, new ProcessPSetRequestHandler());



        container.logger().info("Pas Server started");

    }
}
