package net.ramponi.pas;

import net.ramponi.mpp.ProcessURLRequestHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: tony
 * Date: 19/06/2014
 * Time: 11:17
 * Zeez v1
 */
public class ProcessPSetRequestHandler implements Handler<Message<String>> {
    volatile int counter = 0;
    final UUID uuid;
    public ProcessPSetRequestHandler() {
        uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
    @Override
    public void handle(Message<String> message) {
        System.out.println("message = " + message.body());
        message.reply("pas nessage from :" + uuid + " => " + counter++);
    }
}
