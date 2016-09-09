package ru.sbt.orlov.net;

import java.time.LocalDate;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.reflect.Proxy.newProxyInstance;

public class NetClientFactory {
    private final String host;
    private final int port;

    public NetClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        testCalculatorRMI();
        testWorkerRMI();
    }

    private static void testWorkerRMI() {
        NetClientFactory factory = new NetClientFactory("127.0.0.1", 6000);
        Worker client1 = factory.createClient(Worker.class);
        Worker client2 = factory.createClient(Worker.class);
        Worker client3 = factory.createClient(Worker.class);
        System.out.println(client1.calculateHardThing(2.0d, LocalDate.now()));
        System.out.println(client2.calculateHardThing(3.0d, LocalDate.now()));
        System.out.println(client3.calculateHardThing(4.0d, LocalDate.now()));
        System.out.println(client3.giveMeString());
    }

    private static void testCalculatorRMI() {
        NetClientFactory factory = new NetClientFactory("127.0.0.1", 5000);
        Calculator client = factory.createClient(Calculator.class);
        System.out.println(client.calculate(1, 2));
        try {
            System.out.println(client.calculate(1, 2, LocalDate.now()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T createClient(Class<T> interfaceClass) {
        return (T) newProxyInstance(getSystemClassLoader(), new Class[]{interfaceClass}, new ClientInvocationHandler(host, port));
    }
}
