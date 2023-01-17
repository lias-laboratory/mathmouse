package fr.ensma.lias.dbgui.gui.views;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledEditorKit;

public abstract class AbstractDetailedView extends JComponent {
    protected JTextPane   contentPane;
    protected JScrollPane contentPaneScrollPane;

    public AbstractDetailedView() {
        contentPane = new JTextPane();
        contentPane.setEditable( false );
        contentPane.setVisible( true );
        contentPane.setEditorKitForContentType( "text/xml", new StyledEditorKit() );
        contentPane.setContentType( "text/xml" );
        setLayout( new BorderLayout() );

        contentPaneScrollPane = new JScrollPane( contentPane );
        contentPaneScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        contentPaneScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        contentPaneScrollPane.setVisible( true );

        add( contentPaneScrollPane, BorderLayout.CENTER );
        setVisible( true );
    }

}
