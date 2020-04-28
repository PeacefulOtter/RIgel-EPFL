package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class TimeAnimator extends AnimationTimer
{
    private TimeAccelerator accelerator;
    private SimpleBooleanProperty running;
    private DateTimeBean dateTimeB;
    private long timer;

    public TimeAnimator( DateTimeBean dateTimeB )
    {
        this.dateTimeB = dateTimeB;
        accelerator = null;
        running = new SimpleBooleanProperty( false );
    }

    public void setAccelerator( TimeAccelerator accelerator )
    {
        this.accelerator = accelerator;
    }

    @Override
    public void handle(long l) {
        timer += l;
        dateTimeB.setZonedDateTime( accelerator.adjust(dateTimeB.getZonedDateTime(), timer) );
    }

    public void start()
    {
        if( accelerator == null ) throw new IllegalArgumentException();
        super.start();
        running.setValue( true );
        timer = 0;
    }

    public BooleanExpression isRunning(){
        return ReadOnlyBooleanProperty.booleanExpression(running);
    }

    public void stop(){
        super.stop();
        running.setValue( false );
        timer = 0;
    }
}
