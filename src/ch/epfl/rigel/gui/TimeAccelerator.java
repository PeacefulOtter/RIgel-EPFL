package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/*
    represents a "time accelerator", i.e. a function that calculates simulated - and usually accelerated - time based on real time.
 */
@FunctionalInterface
public interface TimeAccelerator
{
    /**
     * @param initialSimulatedTime : T0, the initial simulated time
     * @param deltaRealTime        : (t-t0), the real time elapsed since the beginning of the animation (in nanoseconds)
     * @return the simulated time (T) and returns it as a new instance of ZonedDateTime
     */
    ZonedDateTime adjust( ZonedDateTime initialSimulatedTime, long deltaRealTime );

    /**
     * @param acceleratorFactor : time acceleration factor
     * @return a continuous accelerator as a function of the acceleration factor
     */
    static TimeAccelerator continous( int acceleratorFactor )
    {
        return ( initialSimulatedTime, deltaRealTime ) ->
        {
            initialSimulatedTime.plusNanos( acceleratorFactor * deltaRealTime );
            return ZonedDateTime.of(
                    initialSimulatedTime.getYear(),
                    initialSimulatedTime.getMonthValue(),
                    initialSimulatedTime.getDayOfMonth(),
                    initialSimulatedTime.getHour(),
                    initialSimulatedTime.getMinute(),
                    initialSimulatedTime.getSecond(),
                    initialSimulatedTime.getNano(),
                    initialSimulatedTime.getZone()
            );
        };
    }

    /**
     * @param advancementFrequency : simulated time stepping frequency
     * @param steps                : discrete step of simulated time
     * @return a discrete accelerator as a function of the advancement frequency and steps
     */
    static TimeAccelerator discrete( int advancementFrequency, Duration steps )
    {
        return ( initialSimulatedTime, deltaRealTime ) ->
        {
            initialSimulatedTime.plusNanos( (long) ( Math.floor( advancementFrequency * deltaRealTime ) * steps.toNanos() ) );
            return ZonedDateTime.of(
                    initialSimulatedTime.getYear(),
                    initialSimulatedTime.getMonthValue(),
                    initialSimulatedTime.getDayOfMonth(),
                    initialSimulatedTime.getHour(),
                    initialSimulatedTime.getMinute(),
                    initialSimulatedTime.getSecond(),
                    initialSimulatedTime.getNano(),
                    initialSimulatedTime.getZone()
            );
        };
    }
}
