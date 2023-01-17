package fr.ensma.lias.dbgui.graphicwrapper.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData;
import fr.ensma.lias.dbgui.gui.views.EquationsListView;
import rx.Observer;

public class EquationIDDataListObserver implements Observer<EquationIDData> {
    private EquationsListView view;
    private Logger            logger = LoggerFactory.getLogger( getClass() );

    public EquationIDDataListObserver( EquationsListView view ) {
        this.view = view;
    }

    public EquationsListView getListView() {
        return view;
    }

    @Override
    public void onCompleted() {
        logger.debug( "Actions completed..." );
    }

    @Override
    public void onError( Throwable arg0 ) {
        logger.debug( "Error occured : " + arg0.getMessage() );
        arg0.printStackTrace();
    }

    @Override
    public void onNext( EquationIDData object ) {
        logger.debug( "Adding element " + object + " in equations list..." );
        view.addElement( object );
        logger.debug( "Adding element " + object + " in equations list... done." );
    }
}
