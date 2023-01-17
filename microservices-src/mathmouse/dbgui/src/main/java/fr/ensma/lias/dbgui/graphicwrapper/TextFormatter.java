package fr.ensma.lias.dbgui.graphicwrapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dbgui.kernel.models.Result;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TextFormatter {
    public static final String XSD_FILE_PATH = "../src/main/resources/differential-equation-2.0.xsd";

    public static String writeFlatDifferentialEquation( DifferentialEquation2 equation ) {
        String flatEquation = new String();
        // writing equation's name and ID
        flatEquation = "Equation : " + equation.getName() + "\n";
        // writing formula
        flatEquation = flatEquation + "Formula : " + equation.getEquation().toString() + "\n";
        // writing equation's variables values
        if ( !equation.getParametersSet().getMapping().isEmpty() ) {
            flatEquation = flatEquation + "Values of the variables : \n";
            for ( String key : equation.getParametersSet().getMapping().keySet() ) {
                flatEquation = flatEquation + "\t" + key + " = " + equation.getParametersSet().getMapping().get( key )
                        + "\n";
            }
        }
        // writing initial conditions
        flatEquation = flatEquation + "Initial conditions : \n";
        Iterator<FunctionKey> iterator = equation.getInputFunctions().keySet().iterator();
        // time origin
        flatEquation = flatEquation + "\ttime origin = "
                + equation.getInputFunctions().get( iterator.next() ).firstKey() + "\n";
        // initial values of the output function
        if ( !equation.getParametersSet().getInitialValues().isEmpty() ) {
            for ( FunctionKey key : equation.getParametersSet().getInitialValues().keySet() ) {
                flatEquation = flatEquation + "\t" + key.getName() + "(" + key.getDeriv() + ") = "
                        + equation.getParametersSet().getInitialValues().get( key ) + "\n";
            }
        }
        // input functions values
        flatEquation = flatEquation + "\tInput functions values : \n";
        for ( FunctionKey key : equation.getInputFunctions().keySet() ) {
            // metadata
            flatEquation = flatEquation + "\t\t" + key.getName() + "(" + key.getDeriv() + ")\n";

            flatEquation = flatEquation + writeFlatTimeSeries( equation.getInputFunctions().get( key ) );
        }
        return flatEquation;
    }

    public static String writeFlatTimeSeries( TimeSeriesDoubleDouble timeSeries ) {
        String flatSeries = "";
        flatSeries = flatSeries + "interpolation method : "
                + timeSeries.getInterpolationFunction().getInterpolationFunctionName().value() + "\n";
        // values
        for ( Double timeKey : timeSeries.keySet() ) {
            flatSeries = flatSeries + "\t\t\t" + timeKey + " -> " + timeSeries.get( timeKey ) + "\n";
        }

        return flatSeries;
    }

    public static String xmlContent( File xmlFile ) {
        String text = "";
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
            StreamResult result = new StreamResult( new StringWriter() );
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse( xmlFile );
            DOMSource source = new DOMSource( document );
            transformer.transform( source, result );
            text = result.getWriter().toString();
        } catch ( IOException | TransformerFactoryConfigurationError
                | ParserConfigurationException | SAXException | TransformerException e ) {
            e.printStackTrace();
        }
        return text;
    }

    public static String xmlContent( String xmlString ) {
        String text = "";
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
            StreamResult result = new StreamResult( new StringWriter() );
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder
                    .parse( new InputSource( new ByteArrayInputStream( xmlString.getBytes( "utf-8" ) ) ) );
            DOMSource source = new DOMSource( document );
            transformer.transform( source, result );
            text = result.getWriter().toString();
        } catch ( IOException | TransformerFactoryConfigurationError
                | ParserConfigurationException | SAXException | TransformerException e ) {
            e.printStackTrace();
        }
        return text;
    }

    public static String writeTimeSeries( TimeSeriesDoubleDouble series ) {
        String text = "";
        text = "interpolation method : " + series.getInterpolationFunction().getInterpolationFunctionName().value()
                + "\n";
        text = text + "values : " + "\n";
        for ( Double key : series.keySet() ) {
            text = text + "\t" + key + " -> " + series.get( key ) + "\n";
        }
        return text;
    }

    public static String writeFlatJobModelDetail( JobModel jobModel ) {
        StringBuffer text = new StringBuffer( "" );

        text.append( "Job's name : " + ( jobModel.getJobsName() == null ? "" : jobModel.getJobsName() ) + "\n" );
        text.append( "Current state : " + ( jobModel.getStatus() == null ? "" : jobModel.getStatus() ) + "\n" );
        text.append( "File name : " + ( jobModel.getFilePath() == null ? "" : jobModel.getFilePath() ) + "\n" );

        if ( !jobModel.getJobResult().getResults().isEmpty() ) {
            text.append(
                    "Number of processed equations : " + jobModel.getJobResult().getNbExpectedEquations() + "\n" );

            for ( Result result : jobModel.getJobResult().getResults() ) {
                text.append( "\t" + "Equation " + result.getEquationName() + " : " + result.getFormula() + "\n" );
                text.append( "\t" + "Average Error : " + result.getAvgError() + "\n" );
                text.append( "\t" + "Minimum Error : " + result.getMinError() + "\n" );
                text.append( "\t" + "Maximum Error : " + result.getMaxError() + "\n" );
                text.append( "\t" + "Standard deviation : " + result.getSdError() + "\n" );
                text.append( "\t" + "Notification : " + result.getNotification().value() + "\n\n" );
            }
        }

        return text.toString();
    }

    public static String writeResultDetail( Result result ) {
        StringBuffer text = new StringBuffer( "" );

        text.append( "Equation " + result.getEquationName() + " : " + result.getFormula() + "\n" );
        text.append( "Average Error : " + result.getAvgError() + "\n" );
        text.append( "Minimum Error : " + result.getMinError() + "\n" );
        text.append( "Maximum Error : " + result.getMaxError() + "\n" );
        text.append( "Standard deviation : " + result.getSdError() + "\n" );
        text.append( "Notification : " + result.getNotification().value() + "\n\n" );

        return text.toString();
    }
}
