package fr.ensma.lias.dbgui.graphicwrapper.models;

import javax.swing.table.DefaultTableModel;

public class DockerControlTableModel extends DefaultTableModel {
    public static final String    SERVICES_NAMES_COLUMN             = "Services";
    public static final String    CONTROL_BUTTON_MINUS_COLUMN       = "Decrease";
    public static final String    CONTROL_BUTTON_PLUS_COLUMN        = "Increase";
    public static final String    MAIN_FIELD_COLUMN                 = "";

    public static final int       SERVICES_NAMES_COLUMN_INDEX       = 0;
    public static final int       CONTROL_BUTTON_MINUS_COLUMN_INDEX = 1;
    public static final int       CONTROL_BUTTON_PLUS_COLUMN_INDEX  = 2;
    public static final int       MAIN_FIELD_COLUMN_INDEX           = 3;

    private static final String[] HEADER                            = { SERVICES_NAMES_COLUMN,
            CONTROL_BUTTON_MINUS_COLUMN, CONTROL_BUTTON_PLUS_COLUMN, MAIN_FIELD_COLUMN };

    public DockerControlTableModel() {
        setColumnIdentifiers( HEADER );
    }

}
