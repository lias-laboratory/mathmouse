package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

public interface IXMLFileIO {
    void writer( String xml, DifferentialEquation eqdiff, String dtdpathfile );

    DifferentialEquation reader( String file, int systemIndex, boolean check ) throws Exception;

}
