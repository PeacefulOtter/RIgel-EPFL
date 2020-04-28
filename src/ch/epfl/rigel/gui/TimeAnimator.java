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
    private final static SimpleBooleanProperty TRUE = new SimpleBooleanProperty( true );
    private final static SimpleBooleanProperty FALSE = new SimpleBooleanProperty( false );

    public TimeAnimator( DateTimeBean dateTimeB )
    {
        this.dateTimeB = dateTimeB;
        accelerator = null;
        running = FALSE;
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
        running = TRUE;
        timer = 0;
    }

    public BooleanExpression isRunnig(){
        return ReadOnlyBooleanProperty.booleanExpression(running);
    }

    public void stop(){
        super.stop();
        running = FALSE;
        timer = 0;
    }
}
