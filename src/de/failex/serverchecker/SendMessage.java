package de.failex.serverchecker;

import javax.swing.JOptionPane;

public class SendMessage {

	public SendMessage(String msg, String title, int style) {
		JOptionPane.showMessageDialog(Main.main, msg, title, style);
	}
}
