package com.sblinn.employee_database.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


/**
 * 
 * @author sarablinn
 *
 */
public class DisplayMethods {

	private final static Font defaultFont = new JLabel().getFont();
	
	
	/**
	 * Prepares components to display a <code>String</code> message in a  
	 * <code>JLabel</code> and <code>JPanel</code>, and using a specified 
	 * <code>LayoutManager</code>, does not repack the frame or set to visible. 
	 * 
	 * <p><b>NOTE:</b> the <code>JPanel</code> specified for <code>displayPanel</code>
	 * should be <code>JPanel</code> specifically for displaying error/warning 
	 * messages and should not contain any other text or components as they will be
	 * removed in this method. Use <code>DisplayMethods.displayMessages()</code> 
	 * to avoid that.
	 * 
	 * @param message <code>String</code>
	 * @param messageLbl <code>JLabel</code>
	 * @param displayPanel <code>JPanel</code>
	 * @param frame <code>JFrame</code>
	 * @param layoutManager <code>LayoutManager</code>
	 * @param color <code>Color</code>
	 */
	public static void setUpMessage(String message, JLabel messageLbl, JPanel displayPanel, 
			LayoutManager layoutManager, Color color) {
		
		if(displayPanel != null) {
			displayPanel.removeAll();
		}
		messageLbl.setText(message);
		messageLbl.setForeground(color);
		displayPanel.add(messageLbl);
		displayPanel.setLayout(layoutManager);
	}
	
	
	/**
	 * Displays a <code>String</code> message in a specified <code>JLabel</code> and
	 * <code>JPanel</code>, and using a specified <code>LayoutManager</code>.
	 * 
	 * <p><b>NOTE:</b> the <code>JPanel</code> specified for <code>displayPanel</code>
	 * should be <code>JPanel</code> specifically for displaying error/warning 
	 * messages and should not contain any other text or components as they will be
	 * removed in this method. Use <code>DisplayMethods.displayMessages()</code> 
	 * to avoid that.
	 * 
	 * @param message <code>String</code>
	 * @param messageLbl <code>JLabel</code>
	 * @param displayPanel <code>JPanel</code>
	 * @param frame <code>JFrame</code>
	 * @param layoutManager <code>LayoutManager</code>
	 * @param color <code>Color</code>
	 */
	public static void displayMessage(String message, JLabel messageLbl, JPanel displayPanel, 
			JFrame frame, LayoutManager layoutManager, Color color) {
		
		clearDisplayPanel(displayPanel, frame);
		messageLbl.setText(message);
		messageLbl.setForeground(color);
		
		displayPanel.add(messageLbl);
		displayPanel.setLayout(layoutManager);
		
		frame.getContentPane().add(displayPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/**
	 * Displays a red <code>String</code> message in a specified <code>JLabel</code> and
	 * <code>JPanel</code>, using a default <code>MigLayout</code> manager.
	 * 
	 * @see #ResetPasswordListener
	 * 
	 * @param message <code>String</code>
	 * @param messageLbl <code>JLabel</code>
	 * @param displayPanel <code>JPanel</code>
	 * @param frame <code>JFrame</code>
	 */
	public static void displayMessage(String message, JLabel messageLbl, JPanel displayPanel, 
			JFrame frame) {
		LayoutManager layoutMgr = new MigLayout(new LC().wrap().alignX("center"));
		displayMessage(message, messageLbl, displayPanel, frame, layoutMgr, Color.RED);
	}
	
	
	/**
	 * Displays a <code>String</code> message in a <code>JTextPane</code>, which will
	 * be contained in a given <code>JPanel</code>.&nbsp;
	 * Creates a <code>JTextPane</code> to display the text using <code>HTML</code> 
	 * to format and style the text according to the parameters.
	 * 
	 * <p><code>colorCode</code> can be any HTML color value, such as, predefined color 
	 * names or RGB, HEX, HSL, RGBA, or HSLA values</p>
	 * <p>Best use for single layout messages which may take up multiple lines and 
	 * therefore need to be spaced by some parent container size. </p>
	 * 
	 * @param message <code>String</code>
	 * @param displayPanel <code>JPanel</code> parent container for the new <code>JTextPane</code>
	 * @para frame <code>JFrame</code>
	 * @param font <code>Font</code>
	 * @param colorCode <code>String</code>
	 */
	public static void displayHTMLMessage(String message, JPanel displayPanel, JFrame frame,
			Font font, String colorCode) {
		clearDisplayPanel(displayPanel, frame);
		JTextPane messagePane = new JTextPane();
		messagePane.setMinimumSize(new Dimension(225, 0));
		messagePane.setMaximumSize(new Dimension(225, 100));
		messagePane.setEditable(false);
		messagePane.setBackground(null);
		messagePane.setContentType("text/html");
		// if no font specified, use system default font
		if(font == null) {
			font = defaultFont;
		}
	
		String htmlMessage = "<div style=\" font-family:" + font.getName() + "; " 
				+ "font-size:" + font.getSize() + "pt" + "; " + "color:" + colorCode + "; "
				+ "padding:0; margin:0\">" 
				+ message + "</div>";
		messagePane.setText(htmlMessage);
		
		displayPanel.add(messagePane);
		frame.getContentPane().add(displayPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/**
	 * Removes all content from a given <code>JPanel</code> on a given 
	 * <code>JFrame</code>.
	 * 
	 * @param displayPanel <code>JPanel</code>
	 * @param frame <code>JFrame</code>
	 */
	public static void clearDisplayPanel(JPanel displayPanel, JFrame frame) {
		if(displayPanel != null) {
			displayPanel.removeAll();
		}
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/**
	 * Determines proper font size for <code>JLabel</code> text based on the
	 * width of the <code>JPanel</code> parent component containing the 
	 * <code>JLabel</code>.
	 * 
	 * @param component <code>JLabel</code> 
	 * @param parent <code>JPanel</code>, containing the <code>JLabel</code> 
	 * @return int font size
	 */
	public static int getProperFontSize(JLabel component, JPanel parent) {
		Font labelFont = component.getFont(); 
		String labelText = (String) component.getText(); 
		
		int stringWidth = component.getFontMetrics(labelFont).stringWidth(labelText); 		
		int componentWidth = parent.getWidth (); // how much the font can grow in width. 		
		double widthRatio = (double)componentWidth / (double)stringWidth;
				
		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		return newFontSize;
	}
	
	/**
	 * Determines and returns the proper font size for the name label in the 
	 * <code>MenuTitlePanel</code> in <code>AdminUI</code> and <code>EmployeeUI</code> 
	 * by determining how much available space is remaining in the panel after the 
	 * <i>Welcome</i> label.
	 * 
	 * <p>
	 * The first parameter, <code>label</code>, is the label which needs a 
	 * proper font sized determined for it. <code>firstLbl</code> is the 
	 * <i>Welcome</i> label, which is taking up <i>x</i> amount of space in the
	 * parent <code>container</code>.
	 * </p>
	 * 
	 * @param label <code>JLabel</code> 
	 * @param firstLbl <code>JLabel</code>  
	 * @param container <code>JPanel</code> parent container
	 * @return int font size
	 */
	public static int getNameLblFontSize(JLabel label, JLabel firstLbl, JPanel container) {
		// Pick a new font size so it will not be larger than the height of Panel.
		int availableHeight = container.getHeight() -  firstLbl.getHeight();
		int newFontSize = Math.min(getProperFontSize(label, container), 
				availableHeight);
		
		return newFontSize;
	}
}
