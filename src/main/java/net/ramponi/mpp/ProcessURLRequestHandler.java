package net.ramponi.mpp;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;

import static net.ramponi.Constants.PAS_MESSAGE_HANDLER;

/**
 * Created with IntelliJ IDEA.
 * User: tony
 * Date: 19/06/2014
 * Time: 09:54
 * Zeez v1
 */
public class ProcessURLRequestHandler implements Handler<Message<String>> {
    volatile int counter = 0;
    EventBus eb;

    public ProcessURLRequestHandler(EventBus eb) {
        this.eb = eb;
    }

    @Override
    public void handle(final Message<String> message) {
        System.out.println("handle message" + message);
        // decompose dset in pset
        // look in the in the global cache
        // ask pas if not present


        eb.send(PAS_MESSAGE_HANDLER, message.body(), new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> pasMessage) {

                message.reply(message.body() + " : " + counter++ + " " + Thread.currentThread().getName() + " ===> " + pasMessage.body());
            }
        });
    }
}
