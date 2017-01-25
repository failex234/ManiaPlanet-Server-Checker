package de.failex.serverchecker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GamePathConfigurator extends JFrame implements ActionListener {
	JTextField path;
	JLabel gamepathtitle;
	JButton confirm;

	public GamePathConfigurator() {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		path = new JTextField();
		path.setLocation(10, 25);
		path.setSize(350, 20);
		path.setText("");
		path.setColumns(10);
		getContentPane().add(path);

		gamepathtitle = new JLabel();
		gamepathtitle.setLocation(9, 4);
		gamepathtitle.setSize(350, 20);
		gamepathtitle.setText("Pfad zur Haupt Exe (ManiaPlanet.exe):");
		getContentPane().add(gamepathtitle);

		confirm = new JButton();
		confirm.setLocation(10, 47);
		confirm.setSize(350, 30);
		confirm.setText("Pfad setzen");
		getContentPane().add(confirm);
		confirm.addActionListener(this);

		setTitle("Exe Pfad setzten");
		setSize(380, 110);
		setVisible(true);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			File game = new File(path.getText());
			if (path.getText().equals("")) {
				SendMessage sm = new SendMessage("Es wurde kein Pfad angegeben, bitte den Pfad zur Haupt Exe eingeben!",
						"Kein Pfad angegeben!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!path.getText().contains("ManiaPlanet.exe")) {
				SendMessage sm = new SendMessage("Du musst den Pfad zu der ManiaPlanet.exe angeben!",
						"Kein richtigen Pfad angegeben!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!game.exists()) {
				SendMessage sm = new SendMessage(
						"Unter dem Pfad " + path.getText()
								+ " konnte das Spiel nicht gefunden werden!\nBitte den Pfad nochmal überprüfen!",
						"Spiel nicht gefunden!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ConfigManager cfg = new ConfigManager();
			try {
				cfg.saveToConfig("gamepath", path.getText());
			} catch (IOException ef) {
				ef.printStackTrace();
			}
			SendMessage sm = new SendMessage(
					"Der Pfad zur Haupt Exe wurde erfolgreich gesetzt.\nBei Klick auf OK wird die Haupt-Oberfläche gestartet",
					"Pfad erfolgreich gesetzt!", JOptionPane.WARNING_MESSAGE);
			Main main = new Main(null);
			setVisible(false);
		}

	}
}
