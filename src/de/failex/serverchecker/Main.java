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

/*
* I know that it's obviously better to use JavaFX instead of
* swing but at that time i didn't knew anything about JavaFX
*/
@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {

	JCheckBox status;
	JLabel statustitle;
	JButton connect;
	JTextField selfconnect;
	JLabel selfconnecttitle;
	String connectionURL = "192.168.178.33::2350@TMCanyon"; //IP of the Gameserver
	static boolean serverup = false;
	static boolean gmup = false;
	boolean allowed = false;
	public static Main main;
	static File game = new File("C:\\Program Files (x86)\\ManiaPlanet\\ManiaPlanet.exe"); //Path to the Game
	static File config = new File("tm2config.yml");

	public Main(String[] args) {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(3);
		status = new JCheckBox();
		status.setLocation(124, 0);
		status.setSize(240, 50);
		if (serverup && gmup) {
			status.setText("Server is online!");
			status.setSelected(true);
			status.setToolTipText("The Server is running and online!");
		} else if (serverup && !gmup) {
			status.setText("Server is running");
			status.setSelected(false);
			status.setToolTipText("The Server is running but not online");
		} else if (!serverup) {
			status.setText("Sever is down");
			status.setSelected(false);
			status.setToolTipText("It seems like the server is down");
		}
		status.addActionListener(this);
		getContentPane().add(status);

		statustitle = new JLabel();
		statustitle.setLocation(0, 0);
		statustitle.setSize(120, 50);
		statustitle.setText("Serverstatus:");
		getContentPane().add(statustitle);

		connect = new JButton();
		connect.setLocation(6, 53);
		connect.setSize(100, 50);
		connect.setText("Connect");
		connect.addActionListener(this);
		getContentPane().add(connect);

		selfconnect = new JTextField();
		selfconnect.setLocation(144, 68);
		selfconnect.setSize(200, 30);
		if (gmup) {
			selfconnect.setText("maniaplanet://#join=" + connectionURL);
		} else {
			selfconnect.setText("Server is not online");
			selfconnect.setEnabled(false);
			;
		}
		selfconnect.setColumns(10);
		getContentPane().add(selfconnect);

		selfconnecttitle = new JLabel();
		selfconnecttitle.setLocation(156, 48);
		selfconnecttitle.setSize(170, 20);
		selfconnecttitle.setText("ManiaPlanet connect code:");
		getContentPane().add(selfconnecttitle);

		setTitle("TrackMania² Server Checker");
		setSize(380, 140);
		setVisible(true);
		setResizable(false);
	}

	public static void main(String[] args) {
		String ip = "tm2"; //IP / Hostname of the Server
		URL url;
		/*Server needs to have a webserver installed and has to
		* have the file 'check.html' just with the string
		* 'true' or 'false' depending on wheter the dedicated
		* server is running or not. At that time i had no idea
		* of PHP and i just created a start-script that changed
		* to true or false, it's easier and better when you use
		* PHP. Maybe i will update the code for that.
		*/
		String serverchecker = "http://" + ip + "/check.html"; 		
		try {
			/*
			* Pings the server to tell if the server is running
			*/
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
						"Gamefile not found under C:\\Program Files (x86)\\ManiaPlanet\\ManiaPlanet.exe. Please choose the path to the ManiaPlanet exe file in the upcoming window!",
						"Game not found!", JOptionPane.WARNING_MESSAGE);
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
						"The server is not running so you can't connect!",
						"Server is not running", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "The server is offline so you can't connect",
						"Server is offline", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
