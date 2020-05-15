package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.*;

public final class TimeAnimator extends AnimationTimer
{
    // ReadOnlyBooleanProperty running = ...
    private final SimpleBooleanProperty running = new SimpleBooleanProperty( false );
    private final DateTimeBean simulatedTimeBean;
    private ZonedDateTime simulatedStart;

    private ObjectProperty<TimeAccelerator> acceleratorProperty = new SimpleObjectProperty<>( null );

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

    @Override
    public void handle( long now )
    {
        if ( firstTimeHandle )
        {
            simulatedStartTime = now; // t0
            firstTimeHandle = false;
            return;
        }
        long deltaRealTime = now - simulatedStartTime; // t - t0
        simulatedTimeBean.setZonedDateTime( getAccelerator().adjust( simulatedStart, deltaRealTime ) );
    }

    public void start()
    {
        if ( getAccelerator() == null ) throw new IllegalArgumentException();
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
