package fr.ensma.lias.dbgui.graphicwrapper.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.Result;
import fr.ensma.lias.timeseriesreductorslib.comparison.ENotification;
import rx.Observer;

public class JobResultObserver implements Observer<String> {
    private static final int METADATA_HASH_INDEX               = 0;
    private static final int METADATA_EXPECTED_EQUATIONS_INDEX = 1;
    private static final int METADATA_ID_INDEX                 = 2;
    private static final int METADATA_EQUATION_NAME_INDEX      = 3;
    private static final int METADATA_EQUATION_FORMULA_INDEX   = 4;

    private static final int DATA_AVG_ERROR_INDEX              = 0;
    private static final int DATA_MIN_ERROR_INDEX              = 1;
    private static final int DATA_MAX_ERROR_INDEX              = 2;
    private static final int DATA_SD_ERROR_INDEX               = 3;
    private static final int DATA_NOTIFICATION_ERROR_INDEX     = 4;

    private Logger           logger;

    public JobResultObserver() {
        super();
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void onCompleted() {
        logger.debug( "Actions completed..." );
    }

    @Override
    public void onError( Throwable e ) {
        logger.debug( "Error occured : " + e.getMessage() );
        e.printStackTrace();
    }

    @Override
    public void onNext( String t ) {
        String message = t.replace( "\t", "" );
        String[] lines = message.split( "\n" );
        String[] metadata = lines[0].split( ";" );
        String[] data = lines[1].split( ";" );

        logger.debug( "received response for model : " + lines[0] );
        int hash = Integer.parseInt( metadata[METADATA_HASH_INDEX] );

        if ( DbUserInterfaceMain.getStartedJobManager().contains( hash ) ) {
            DbUserInterfaceMain.getStartedJobManager().incrementeProcessedEquations( hash );

            Result result = new Result( metadata[METADATA_EQUATION_NAME_INDEX],
                    metadata[METADATA_EQUATION_FORMULA_INDEX], Long.parseLong( metadata[METADATA_ID_INDEX] ),
                    ENotification.fromValue( data[DATA_NOTIFICATION_ERROR_INDEX] ),
                    Double.parseDouble( data[DATA_AVG_ERROR_INDEX] ), Double.parseDouble( data[DATA_MIN_ERROR_INDEX] ),
                    Double.parseDouble( data[DATA_MAX_ERROR_INDEX] ), Double.parseDouble( data[DATA_SD_ERROR_INDEX] ) );

            DbUserInterfaceMain.getStartedJobManager().addJobResult( hash, result );
            DbUserInterfaceMain.getStartedJobManager().fireJobProgress( hash );
        }

    }

}
