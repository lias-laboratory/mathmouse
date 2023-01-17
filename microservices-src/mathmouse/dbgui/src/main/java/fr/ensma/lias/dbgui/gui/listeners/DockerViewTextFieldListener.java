package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

public class DockerViewTextFieldListener implements ActionListener {
    private JTextField   textField;
    private EServiceName service;

    public DockerViewTextFieldListener( JTextField field, EServiceName serviceName ) {
        textField = field;
        service = serviceName;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        try {
            String text = textField.getText();

            if ( text.matches( "-?\\d+?" ) ) {
                int newInstancesCommand = Integer.parseInt( textField.getText() );
                DbUserInterfaceMain.getSender().publish( EQueueName.SCALE_SERVICE_REQUEST,
                        service.value() + ESpecialCharacter.SEPARATOR.value() + newInstancesCommand );
            }

        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
    }
}
