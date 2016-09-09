package ru.sbt.orlov.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * Client:
 * createProxy
 *     send method name + args to server
 *    receive return value from server and return it
 * */

/*
 * server:
 * listen host + port
 * read methodName + args
 * invoke method via reflection
 * send return value to client
 *
 * */
public class ServerRegistrator {

    public static void listen(String host, int port, Object impl) throws UnknownHostException, IOException {
        InetAddress hostAdress = InetAddress.getByName(host);
        String threadName = "Thread listening "+impl.getClass().getName();
        new ListeningThread(threadName, hostAdress, port, impl).start();
    }

    // Регистрируем обработчики вызовов.
    public static void main(String[] args) throws IOException {
        ServerRegistrator.listen("127.0.0.1", 5000, new CalculatorImpl());
    }
}
