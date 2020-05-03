package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.*;

public final class TimeAnimator extends AnimationTimer
{
    // ReadOnlyBooleanProperty running = ...
    private final SimpleBooleanProperty running = new SimpleBooleanProperty( false );
    private final DateTimeBean simulatedTimeBean;
    private ZonedDateTime simulatedStart;

    private TimeAccelerator accelerator = null;

    private boolean firstTimeHandle = true;
    private long simulatedStartTime;


    public TimeAnimator( DateTimeBean simulatedStartBean )
    {
        this.simulatedTimeBean = simulatedStartBean;
        this.simulatedStart = simulatedStartBean.getZonedDateTime();
    }

    public void setAccelerator( TimeAccelerator accelerator ) { this.accelerator = accelerator; }


    @Override
    public void handle( long now )
    {
        if ( firstTimeHandle )
        {
            simulatedStartTime = now;
            firstTimeHandle = false;
            return;
        }
        long deltaRealTime = now - simulatedStartTime;
        simulatedTimeBean.setZonedDateTime( accelerator.adjust( simulatedStart, deltaRealTime ) );
    }

    public void start()
    {
        if ( accelerator == null ) throw new IllegalArgumentException();
        super.start();
        running.setValue( true );
        firstTimeHandle = true;
    }

    public void stop()
    {
        super.stop();
        running.setValue( false );
    }

    public BooleanExpression isRunning() { return ReadOnlyBooleanProperty.booleanExpression( running ); }

}
