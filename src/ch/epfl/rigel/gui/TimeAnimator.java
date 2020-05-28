package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.*;
/*
    represents a "time animator". Its purpose is to periodically modify, via a time accelerator,
    the instant of observation stored in an instance of DateTimeBean, in order to (indirectly) animate the sky.
 */
public final class TimeAnimator extends AnimationTimer
{
    private final SimpleBooleanProperty running = new SimpleBooleanProperty( false );
    private final ObjectProperty<TimeAccelerator> acceleratorProperty = new SimpleObjectProperty<>( null );
    private final DateTimeBean simulatedTimeBean;
    private ZonedDateTime simulatedStart;

    private boolean firstTimeHandle = true;
    private long simulatedStartTime;


    public TimeAnimator( DateTimeBean simulatedStartBean )
    {
        this.simulatedTimeBean = simulatedStartBean;
        this.simulatedStart = simulatedStartBean.getZonedDateTime(); // T0
    }

    public TimeAccelerator getAccelerator() { return acceleratorProperty.getValue(); }

    public ObjectProperty<TimeAccelerator> acceleratorProperty() { return acceleratorProperty; }

    public void setAccelerator( TimeAccelerator accelerator ) { this.acceleratorProperty.set( accelerator ); }

    public void setDateTimeBean( DateTimeBean dateTimeBean ) {
        this.simulatedTimeBean.setZonedDateTime( dateTimeBean.getZonedDateTime() );
    }

    /**
     * progress the animation by adjusting the accelerator and the dateTimeBean
     * @param now : the number of nanoseconds elapsed since an unspecified start time.
     */
    @Override
    public void handle( long now )
    {
        if ( firstTimeHandle )
        {
            simulatedStart = simulatedTimeBean.getZonedDateTime();
            simulatedStartTime = now; // t0
            firstTimeHandle = false;
            return;
        }

        long deltaRealTime = now - simulatedStartTime; // t - t0
        simulatedTimeBean.setZonedDateTime( getAccelerator().adjust( simulatedStart, deltaRealTime ) );
    }

    /**
     * start the timer of the animation
     */
    public void start()
    {
        if ( getAccelerator() == null ) throw new IllegalArgumentException();
        super.start();
        running.setValue( true );
        firstTimeHandle = true;
    }

    /**
     * stop the timer of the animation
     */
    public void stop()
    {
        super.stop();
        running.setValue( false );
    }

    public BooleanExpression isRunning() { return ReadOnlyBooleanProperty.booleanExpression( running ); }

}
