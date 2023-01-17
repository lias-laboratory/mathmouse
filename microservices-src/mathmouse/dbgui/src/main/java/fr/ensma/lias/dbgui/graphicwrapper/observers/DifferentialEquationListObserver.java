package fr.ensma.lias.dbgui.graphicwrapper.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.TextFormatter;
import fr.ensma.lias.dbgui.graphicwrapper.models.EquationForm;
import fr.ensma.lias.dbgui.gui.views.EquationTabbedView;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import rx.Observer;

public class DifferentialEquationListObserver implements Observer<String> {
    private EquationTabbedView equationTabbedView;
    private Logger             logger;

    public DifferentialEquationListObserver( EquationTabbedView view ) {
        equationTabbedView = view;
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void onCompleted() {
        logger.debug( "complete new equation printing." );
    }

    @Override
    public void onError( Throwable e ) {
        logger.debug( "Error occured : " + e.getMessage() );
    }

    @Override
    public void onNext( String t ) {
        DifferentialEquation2 equation = DifferentialEquation2IO.XMLRead20FromString( TextFormatter.XSD_FILE_PATH, t );
        equationTabbedView.setFlatText( TextFormatter.writeFlatDifferentialEquation( equation ) );
        equationTabbedView.setXMLText( TextFormatter.xmlContent( t ) );
        equationTabbedView.setFormText( new EquationForm( equation ) );
    }

}
