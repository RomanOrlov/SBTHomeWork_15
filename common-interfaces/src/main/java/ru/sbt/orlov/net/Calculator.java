package ru.sbt.orlov.net;

import java.time.LocalDate;

public interface Calculator {
    double calculate(int a, int b);

    double calculate(int a, int b, LocalDate date);
}
