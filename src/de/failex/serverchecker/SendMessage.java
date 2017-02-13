package de.failex.serverchecker;

import javax.swing.JOptionPane;

/*
* Don't ask why i made this class in the first place because i don't know..
*/
public class SendMessage {

	public SendMessage(String msg, String title, int style) {
		JOptionPane.showMessageDialog(Main.main, msg, title, style);
	}
}
