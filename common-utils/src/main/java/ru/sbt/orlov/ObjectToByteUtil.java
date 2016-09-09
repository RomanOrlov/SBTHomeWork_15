package ru.sbt.orlov;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectToByteUtil {
    public static byte[] objectToByteArray(Object object) throws IOException {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objStream = new ObjectOutputStream(arrayOutputStream)) {
            objStream.writeObject(object);
            return arrayOutputStream.toByteArray();
        }
    }
}
