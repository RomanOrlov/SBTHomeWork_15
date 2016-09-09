package ru.sbt.orlov.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.sbt.orlov.ObjectToByteUtil.objectToByteArray;

/**
 * @author r.orlov
 */
public class ListeningThread extends Thread {
    private final static Logger LOGGER = Logger.getLogger(ListeningThread.class.getName());
    private final ServerSocket socket;
    private final Object impl;
    private boolean isStopped;

    public ListeningThread(String threadName, InetAddress address, int port, Object impl) throws IOException {
        super(threadName);
        socket = new ServerSocket(port, 10, address);
        this.impl = impl;
    }

    @Override
    public void run() {
        while (!isStopped) {
            try {
                Socket accept = socket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream());
                Object[] input = (Object[]) inputStream.readObject();
                Object result = invokeMethod(input);
                byte[] buffer = objectToByteArray(result);
                accept.getOutputStream().write(buffer);
            } catch (IOException | SecurityException | IllegalArgumentException | ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Exception occurred in thread ", ex);
            }
        }
    }

    private Object invokeMethod(Object[] input) {
        String methodName = (String) input[0];
        try {
            if (input.length == 1) return impl.getClass().getMethod(methodName).invoke(impl);
            return invokeMethodWithArgs(methodName, input);
        } catch (Exception ex) {
            // Exception from reflection method call wrapped in InvocationTargetException
            return ex.getCause() == null ? ex : ex.getCause();
        }
    }

    private Object invokeMethodWithArgs(String methodName, Object[] input) throws Exception {
        Integer argsLength = (Integer) input[1];
        System.out.println(argsLength);
        Object[] args = Arrays.copyOfRange(input, 2, argsLength + 2);
        Class<?>[] classObjects = Arrays.copyOfRange(input, argsLength + 2, input.length, Class[].class);
        return impl.getClass().getMethod(methodName, classObjects).invoke(impl, args);
    }

    private void stopListening() {
        isStopped = true;
    }
}
