package net.ramponi;

import org.vertx.java.platform.Verticle;

/**
 * Created with IntelliJ IDEA.
 * User: tony
 * Date: 19/06/2014
 * Time: 09:14
 * Zeez v1
 */
public class Main extends Verticle {
    public void start() {
        container.deployVerticle(MppVerticle.class.getName());
        container.deployVerticle(PingVerticle.class.getName());
        container.deployVerticle(PasVerticle.class.getName());
    }
}
