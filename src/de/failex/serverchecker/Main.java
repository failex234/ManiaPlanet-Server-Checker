package de.failex.serverchecker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {

	JCheckBox status;
	JLabel statustitle;
	JButton connect;
	JTextField selfconnect;
	JLabel selfconnecttitle;
	String connectionURL = "192.168.178.33::2350@TMCanyon";
	static boolean serverup = false;
	static boolean gmup = false;
	boolean allowed = false;
	public static Main main;
	static File game = new File("C:\\Program Files (x86)\\ManiaPlanet\\ManiaPlanet.exe");
	static File config = new File("tm2config.yml");

	public Main(String[] args) {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(3);
		status = new JCheckBox();
		status.setLocation(124, 0);
		status.setSize(240, 50);
		if (serverup && gmup) {
			status.setText("Beide Server sind online.");
			status.setSelected(true);
			status.setToolTipText("Der Linux und der Gameserver sind beide gestartet.");
		} else if (serverup && !gmup) {
			status.setText("Server ist online, Gameserver nicht.");
			status.setSelected(false);
			status.setToolTipText("Der Linux Server ist gestartet, der Gameserver aber nicht.");
		} else if (!serverup) {
			status.setText("Beide Server sind offline.");
			status.setSelected(false);
			status.setToolTipText("Der Linux Server scheint nicht oben zu sein.");
		}
		status.addActionListener(this);
		getContentPane().add(status);

		statustitle = new JLabel();
		statustitle.setLocation(0, 0);
		statustitle.setSize(120, 50);
		statustitle.setText("Status des Servers:");
		getContentPane().add(statustitle);

		connect = new JButton();
		connect.setLocation(6, 53);
		connect.setSize(100, 50);
		connect.setText("Verbinden");
		connect.addActionListener(this);
		getContentPane().add(connect);

		selfconnect = new JTextField();
		selfconnect.setLocation(144, 68);
		selfconnect.setSize(200, 30);
		if (gmup) {
			selfconnect.setText("maniaplanet://#join=" + connectionURL);
		} else {
			selfconnect.setText("Server ist offline");
			selfconnect.setEnabled(false);
			;
		}
		selfconnect.setColumns(10);
		getContentPane().add(selfconnect);

		selfconnecttitle = new JLabel();
		selfconnecttitle.setLocation(156, 48);
		selfconnecttitle.setSize(170, 20);
		selfconnecttitle.setText("Code zum selber verbinden:");
		getContentPane().add(selfconnecttitle);

		setTitle("TrackMania² Server Checker");
		setSize(380, 140);
		setVisible(true);
		setResizable(false);
	}

	public static void main(String[] args) {
		String ip = "tm2";
		URL url;
		String serverchecker = "http://tm2/check.html";
		try {
			InetAddress inet = InetAddress.getByName(ip);
			if (inet.isReachable(15)) {
				serverup = true;
			}
			url = new URL(serverchecker);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String checker;
			while ((checker = br.readLine()) != null) {
				if (checker.equals("true")) {
					gmup = true;
				}
			}
			if (config.exists()) {
				ConfigManager cm = new ConfigManager();
				game = new File(cm.con.getString("gamepath"));
				Main mn = new Main(args);
				return;
			}
			if (!game.exists()) {
				SendMessage sm = new SendMessage(
						"Das Spiel wurde unter C:\\Program Files (x86)\\ManiaPlanet\\ManiaPlanet.exe nicht gefunden. Bitte gleich den Pfad zum Spiel eingeben!",
						"Spiel nicht gefunden!", JOptionPane.WARNING_MESSAGE);
				GamePathConfigurator gpc = new GamePathConfigurator();
				return;
			}
		} catch (UnknownHostException e) {
		} catch (MalformedURLException ef) {
		} catch (IOException eff) {
		}
		Main mn = new Main(args);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == status) {
			if (!serverup) {
				status.setSelected(false);
			} else if (serverup && !gmup) {
				status.setSelected(false);
			} else {
				status.setSelected(true);
			}
		} else if (e.getSource() == connect) {
			if (serverup && gmup) {
				try {
					Runtime rt = Runtime.getRuntime();
					rt.exec(game.getPath() + " /join=" + connectionURL);
				} catch (IOException ef) {
					ef.printStackTrace();
				}
				allowed = true;
			} else if (serverup && !gmup) {
				JOptionPane.showMessageDialog(this,
						"Da der Gameserver offline ist, kannst du dich nicht mit ihm verbinden.",
						"Gameserver ist offline", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Die beiden Server sind offline, du kannst nicht verbinden",
						"Beide Server sind offline", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
