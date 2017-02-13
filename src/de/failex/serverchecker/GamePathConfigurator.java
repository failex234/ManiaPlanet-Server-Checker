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
		gamepathtitle.setText("Path to the executable (ManiaPlanet.exe):");
		getContentPane().add(gamepathtitle);

		confirm = new JButton();
		confirm.setLocation(10, 47);
		confirm.setSize(350, 30);
		confirm.setText("Set Path");
		getContentPane().add(confirm);
		confirm.addActionListener(this);

		setTitle("Set executable path");
		setSize(380, 110);
		setVisible(true);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			File game = new File(path.getText());
			if (path.getText().equals("")) {
				SendMessage sm = new SendMessage("Please set the path to your game executable!",
						"No Input!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!path.getText().contains("ManiaPlanet.exe")) {
				SendMessage sm = new SendMessage("This Path is not a direct path to ManiaPlanet.exe!",
						"Wrong path", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!game.exists()) {
				SendMessage sm = new SendMessage(
						"Game was not found under " + path.getText()
								+ ". Please check the path for typos\nAnd try again!",
						"Game not found!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ConfigManager cfg = new ConfigManager();
			try {
				cfg.saveToConfig("gamepath", path.getText());
			} catch (IOException ef) {
				ef.printStackTrace();
			}
			SendMessage sm = new SendMessage(
					"Gamepath successfully set!\nIf you click on OK the main-program will start again",
					"Path set!", JOptionPane.WARNING_MESSAGE);
			Main main = new Main(null);
			setVisible(false);
		}

	}
}
