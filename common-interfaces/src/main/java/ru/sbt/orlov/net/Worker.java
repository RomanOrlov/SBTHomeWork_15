package ru.sbt.orlov.net;

import java.time.LocalDate;

public interface Worker {
    int calculateHardThing(Double d1, LocalDate date);

    String giveMeString();
}
