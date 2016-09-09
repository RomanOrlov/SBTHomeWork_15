package ru.sbt.orlov.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerRegistrator {

    public static void listen(String host, int port, Object impl) throws UnknownHostException, IOException {
        InetAddress hostAdress = InetAddress.getByName(host);
        String threadName = "Thread listening "+impl.getClass().getName();
        new ListeningThread(threadName, hostAdress, port, impl).start();
    }

    // Регистрируем обработчики вызовов.
    public static void main(String[] args) throws IOException {
        ServerRegistrator.listen("127.0.0.1", 5000, new CalculatorImpl());
        ServerRegistrator.listen("127.0.0.1", 6000, new WorkerImpl());
    }
}
