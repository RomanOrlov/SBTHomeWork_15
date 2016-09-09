package ru.sbt.orlov.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

import static ru.sbt.orlov.ObjectToByteUtil.objectToByteArray;

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
            return receiveAnswer(client.getInputStream());
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

    private Object receiveAnswer(InputStream inputStream) throws Throwable {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Object result = objectInputStream.readObject();
            if (result instanceof Throwable) {
                throw (Throwable) result;
            }
            return result;
        }
    }
}
