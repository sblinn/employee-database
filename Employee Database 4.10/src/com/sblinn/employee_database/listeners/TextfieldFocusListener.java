package com.sblinn.employee_database.listeners;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


/**
 * <code>TextfieldFocusListener</code> is an implementation of  
 * <code>FocusListener</code> and <code>CaretListener</code> interfaces, 
 * to be used in text fields which have labels inside of the field.
 * 
 * <code>TextfieldFocusListener</code> clears the label when the user
 * focuses on the field. If text is input and focus is moved away, the
 * new input text will remain. If no new input was added, when focus
 * leaves the field, the original label text will reappear.
 * 
 * @author sarablinn
 *
 */
public class TextfieldFocusListener implements FocusListener {

	private int caretPosition;
	
	
	@Override
	public void focusGained(FocusEvent e) {
		JTextField textField = (JTextField) e.getSource();
		if(textField.getText().equals(textField.getName())) {
			textField.setText("");
			textField.setForeground(Color.DARK_GRAY);
			
			textField.addCaretListener(new CaretListener() {

				@Override
				public void caretUpdate(CaretEvent e) {
					caretPosition = textField.getCaretPosition();
				}
			});
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		JTextField textField = (JTextField) e.getSource();
		if(textField.getText().isEmpty() && caretPosition == 0) {
			textField.setForeground(Color.LIGHT_GRAY);
			textField.setText(textField.getName());
		}		
	}

}
