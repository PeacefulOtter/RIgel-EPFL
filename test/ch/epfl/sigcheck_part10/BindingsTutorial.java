package ch.epfl.sigcheck_part10;


import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import org.junit.jupiter.api.Test;

public final class BindingsTutorial
{
    public static void main( String[] args )
    {
        StringProperty s = new SimpleStringProperty( "Hi!" );
        IntegerProperty b = new SimpleIntegerProperty( 0 );
        IntegerProperty e = new SimpleIntegerProperty( 3 );

        // Bind multiple property (here s, b and e are bind to ss)
        ObservableStringValue ss = Bindings.createStringBinding(
                () -> s.getValue().substring( b.get(), e.get() ),
                s, b, e );
        ss.addListener( o -> System.out.println( ss.get() ) );

        System.out.println( "----" );
        s.set( "Hello, world!" );
        s.setValue( "Bonjour, monde !" );
        e.set( 16 );
        b.set( 9 );
        System.out.println( "----\n\n\n" );
    }

    @Test
    void bindingTuto()
    {
        StringProperty stringProperty1 = new SimpleStringProperty( "imtheoldvalue");
        StringProperty stringProperty2 = new SimpleStringProperty( "imtheNEWvaluenow" );
        stringProperty1.addListener( (observable, oldvalue, newvalue) -> {
            System.out.println( observable + "   " + oldvalue + "   " + newvalue );
        } );
        stringProperty1.bind( stringProperty2 );

        stringProperty2.setValue( "mdrrr boloss ez" );
    }
}
