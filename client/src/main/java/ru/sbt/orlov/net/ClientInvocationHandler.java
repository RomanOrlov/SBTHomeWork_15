package ru.sbt.orlov.net;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientInvocationHandler implements InvocationHandler {
    private final String host;
    private final int port;

    public ClientInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        byte[] toServerBuffer = argsToByteArray(method.getName(), args, method.getParameterTypes());
        try (Socket client = new Socket(host, port)) {
            client.getOutputStream().write(toServerBuffer);
            return recieveAwnser(client.getInputStream());
        }
    }

    private byte[] argsToByteArray(String methodName, Object[] args, Class<?>[] types) throws IOException {
        Object[] toServerArgs = new Object[2 * args.length + 2];
        toServerArgs[0] = methodName;
        toServerArgs[1] = args.length;
        System.arraycopy(args, 0, toServerArgs, 2, args.length);
        System.arraycopy(types, 0, toServerArgs, 2 + args.length, types.length);
        return objectToByteArray(toServerArgs);
    }

    private Object recieveAwnser(InputStream inputStream) throws Throwable {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Object result = objectInputStream.readObject();
            if (result instanceof Throwable) {
                throw (Throwable) result;
            }
            return result;
        }
    }

    private byte[] objectToByteArray(Object object) throws IOException {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objStream = new ObjectOutputStream(arrayOutputStream)) {
            objStream.writeObject(object);
            return arrayOutputStream.toByteArray();
        }
    }
}
