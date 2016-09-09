package ru.sbt.orlov.net;

import java.util.Date;

public interface Calculator {
    double calculate(int a, int b);
    double calculate(int a, int b, Date date);
}
