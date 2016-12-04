package org.afabl.bunny.dialog;

import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class ParticipantIdFormatterFactory
        extends JFormattedTextField.AbstractFormatterFactory {
  @Override
  public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
    try {
      MaskFormatter formatter = new MaskFormatter("########");
      formatter.setPlaceholder("");
      return formatter;
    } catch (ParseException e) {
      return null;
    }
  }
}