package ru.sbt.orlov.net;

import java.time.LocalDate;

public class WorkerImpl implements Worker {
    @Override
    public int calculateHardThing(Double d1, LocalDate date) {
        return (int)(date.getDayOfMonth()*d1);
    }
}
