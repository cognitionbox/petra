package io.cognitionbox.petra.lang;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public final class PollingTimer implements Serializable {
    private final int secondsToPass;
    private final boolean synchronizeToMinuteInterval;

    private volatile LocalDateTime lastTick;

    public PollingTimer(int secondsToPass, boolean synchronizeToMinuteInterval) {
        this.secondsToPass = secondsToPass;
        this.synchronizeToMinuteInterval = synchronizeToMinuteInterval;
    }

    public final boolean periodHasPassed(LocalDateTime tick) {
        if (tick == null) {
            return false;
        } else if (this.lastTick == null) {
            this.lastTick = tick;
            return false;
        } else {
            if (this.synchronizeToMinuteInterval && this.lastTick == null) {
                LocalDateTime now = LocalDateTime.now();
                LocalDate var10000 = LocalDate.now();
                LocalDateTime startingTick = LocalDateTime.of(var10000, LocalTime.of(now.getHour(), now.getMinute(), 0));
                this.lastTick = startingTick;
            }

            long diff = ChronoUnit.SECONDS.between((Temporal) tick, (Temporal) this.lastTick);
            if (Math.abs(diff) >= (long) this.secondsToPass) {
                this.lastTick = tick;
                return true;
            } else {
                return false;
            }
        }
    }

    public final int getSecondsToPass() {
        return this.secondsToPass;
    }

    public final boolean getSynchronizeToMinuteInterval() {
        return this.synchronizeToMinuteInterval;
    }
}
