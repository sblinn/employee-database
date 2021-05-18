package com.sblinn.employee_database.listeners;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


/**
 * A <code>FocusListener</code> for any <code>JPasswordField</code> 
 * which contains its label inside of the text field, it handles what
 * happens when user clicks or begins typing. 
 * 
 * <p>When focus is gained, the label text disappears and the text
 * color is changed to <code>Color.DARK_GRAY</code>. Once the user
 * begins typing, a <code>CaretListener</code> queues a 
 * <code>CaretEvent</code> in which the text characters are changed
 * to disguise the user input.
 * 
 * <p>When focus is lost, i.e. the user clicks away from the field,
 * if the user did not input any new text, the field's label will 
 * reappear in <code>Color.LIGHT_GRAY</code>. If user <i>did</i> input
 * text then the text will remain present and disguised. 
 * 
 * <p><i><b>NOTE: </b>there are no checks in place to make sure the 
 * type of component being listened to is a <code>JPasswordField</code>.
 * </i>
 * 
 * @author sarablinn
 *
 */
public class PasswordFocusListener implements FocusListener {
	
	/**
	 * int representing caret position inside of the <code>JPasswordField</code>
	 * currently in focus.
	 */
	private int caretPosition;
	
	/**
	 * Invoked when a component gains the keyboard focus, clears the text,
	 * sets the color and then disguises the text once the user begins
	 * typing.
	 * 
	 * @param e <code>FocusEvent</code>
	 */
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
					if(caretPosition > 0) {
						((JPasswordField) textField).setEchoChar('●');
					}
				}
			});
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus, resets the
	 * text back to its name (label) and its color back to 
	 * <code>Color.LIGHT_GRAY</code> if no input. If there is input,
	 * then the text remains and the characters remain disguised.
	 * 
	 * @param e <code>FocusEvent</code>
	 */
	@Override
	public void focusLost(FocusEvent e) {
		JPasswordField textField = (JPasswordField) e.getSource();
		if(textField.getPassword().length == 0 && caretPosition == 0) {
			textField.setForeground(Color.LIGHT_GRAY);
			textField.setText(textField.getName());
			textField.setEchoChar((char) 0);
		}
	}
}
