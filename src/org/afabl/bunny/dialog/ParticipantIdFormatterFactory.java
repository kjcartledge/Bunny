package org.afabl.bunny.dialog;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class ParticipantIdFormatterFactory
        extends JFormattedTextField.AbstractFormatterFactory {
    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
        try {
            MaskFormatter formatter = new MaskFormatter("########");
            formatter.setPlaceholder("Enter ID");
            return formatter;
        } catch (ParseException e) {
            return null;
        }
    }
}