package fr.ensma.lias.javarabbitmqapi.enumerations;

public enum EQueueName {
        GET_EQUATIONS_RESPONSE_QUEUE_NAME( "responseGetEquations" ),
        PUT_EQUATION_RESPONSE_QUEUE_NAME( "responsePutEquation" ),
        LOG_MESSAGES_QUEUE_NAME( "logMessages" ),
        SQL_QUERY_RESPONSE_QUEUE_NAME( "SQLQueryResponse" ),
        GENERATE_PULL_RESPONSE_QUEUE_NAME( "generatePullResponse" ),
        GET_EQUATIONS_ID_AND_NAMES_RESPONSE_QUEUE_NAME( "responseGetEquationsIDAndNames" ),
        GET_EQUATIONS_REQUEST_QUEUE_NAME( "requestGetEquations" ),
        PUT_EQUATION_REQUEST_QUEUE_NAME( "requestPutEquation" ),
        SQL_QUERY_REQUEST_QUEUE_NAME( "SQLQueryRequest" ),
        GENERATE_PULL_REQUEST_QUEUE_NAME( "generatePullRequest" ),
        GET_EQUATIONS_ID_AND_NAMES_REQUEST_QUEUE_NAME( "requestGetEquationsIDAndNames" ),
        COMPARISON_RESPONSE_QUEUE_NAME( "comparisonResponse" ),
        COMPARISON_REQUEST_QUEUE_NAME( "comparisonRequest" ),
        GENERATE_REQUEST_QUEUE_NAME( "generateRequest" ),
        GENERATE_RESPONSE_QUEUE_NAME( "generateResponse" ),
        INCREMENTE_SERVICE_REQUEST( "incrementeServiceRequest" ),
        DECREMENTE_SERVICE_REQUEST( "decrementeServiceRequest" ),
        SCALE_SERVICE_REQUEST( "scaleServiceRequest" ),
        INSTANCES_OF_SERVICE_REQUEST( "instancesOfServiceRequest" ),
        INSTANCES_OF_SERVICE_UPDATE( "instancesOfServiceUpdate" ),
        ALL_SERVICES_INSTANCES_REQUEST( "allServicesInstancesRequest" ),
        COMPARISON_PERFORMED( "comparisonPerformed" ),
        COMPARISON_DISPATCH( "comparisonDispatch" ),
        PROGRESS( "progress" ),
        JOB_OVER( "jobOver" );

    private String value;

    private EQueueName( String value ) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static EQueueName getQueueName( String name ) {
        EQueueName[] values = values();

        EQueueName result = null;

        for ( int i = 0; i < values.length; i++ ) {
            if ( values[i].value().equals( name ) )
                result = values[i];
        }

        return result;
    }
}
