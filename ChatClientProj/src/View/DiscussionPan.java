package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import RMI.ChatClient;
import RMI.ChatClientInt;
import RMI.ChatServerInt;

public class DiscussionPan extends JPanel {
	private static final long serialVersionUID = 1L;
	JScrollPane scrolPane;
	public JTabbedPane tabbeClient;
	JTextField ip, login, password;
	public static ChatClient client;
	public static ChatServerInt server;
//	private JList<JLabel> list;
	public Map<String, Integer> indexClient;
	Vector<ChatClientInt> listClient;

	public DiscussionPan() {
		setSize(1000, 700);
		setLayout(new FlowLayout());
		setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
		setOpaque(true);
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
		validate();

		tabbeClient = new JTabbedPane(JTabbedPane.LEFT);
		tabbeClient.addTab("    Connected ... ", new ChatPan());// "../img/home1.png"
		tabbeClient.setSelectedIndex(0);
		tabbeClient.setFont(new Font("Tahoma", Font.BOLD, 16));// setFont(new Font("Segoe UI Semilight", 5, 15));
		tabbeClient.setBackground(new Color(24, 43, 53));// 72, 86, 98
		tabbeClient.setForeground(Color.black);
		tabbeClient.setFocusable(true);
		tabbeClient.validate();

		fillMain();
//		fillDiscution();
	}

	public void fillMain() {
		removeAll();

		JLabel ipLabel = new JLabel("ip :");
		ipLabel.setOpaque(false);
		ipLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		ipLabel.setForeground(new Color(18, 210, 210));// 32,178,170

		String s = "";
		try {
			s = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			s = "";
			e1.printStackTrace();
		}
		ip = new JTextField(s);
		ip.setOpaque(false);
		ip.setFont(new Font("World of Water", Font.LAYOUT_NO_START_CONTEXT, 20));
		ip.setForeground(Color.white);
		ip.setPreferredSize(new Dimension(300, 40));
		ip.setSize(ip.getPreferredSize());

		JLabel loginLabel = new JLabel("login :");
		loginLabel.setOpaque(false);
		loginLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		loginLabel.setForeground(new Color(18, 210, 210));

		login = new JTextField();
		login.setOpaque(false);
		login.setFont(new Font("World of Water", Font.LAYOUT_NO_START_CONTEXT, 20));
		login.setForeground(Color.WHITE);
		login.setPreferredSize(new Dimension(300, 40));
		login.setSize(login.getPreferredSize());

		JLabel passwordLabel = new JLabel("password :");
		passwordLabel.setOpaque(false);
		passwordLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		passwordLabel.setForeground(new Color(18, 210, 210));

		password = new JPasswordField();
		password.setOpaque(false);
		password.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		password.setForeground(Color.WHITE);
		password.setPreferredSize(new Dimension(300, 40));
		password.setSize(password.getPreferredSize());

		JButton ValidButton = new JButton(Manager.getImg("img/Icons/valid.png", 90, 45));// ("Validat");
		ValidButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		ValidButton.setBackground(new Color(0, 0, 0, 0));
		ValidButton.setFocusable(false);
		ValidButton.setOpaque(false);
		ValidButton.setForeground(Color.white);
		ValidButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Connection
				doConnect();

			}
		});

		JPanel pad = new JPanel();
		pad.setBackground(null);
		pad.setPreferredSize(new Dimension(this.getWidth(), 260));
		pad.setOpaque(false);

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(300, 650));
		panel.add(ipLabel);
		panel.add(ip);
		panel.add(loginLabel);
		panel.add(login);
		panel.add(passwordLabel);
		panel.add(password);
		panel.add(ValidButton);

		add(pad, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	public void fillDiscution() {
		removeAll();
		setLayout(new BorderLayout());
		add(tabbeClient, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	@SuppressWarnings("unchecked")
	public void doConnect() {
		if (login.getText().length() < 2) {
			JOptionPane.showMessageDialog(this, "You need to type a name.");
			return;
		}
		if (ip.getText().length() < 2) {
			JOptionPane.showMessageDialog(this, "You need to type an IP.");
			return;
		}
		try {
			client = new ChatClient(login.getText());
			client.setDiscussionPan(this);
			server = (ChatServerInt) Naming.lookup("rmi://" + ip.getText().toString() + "/chat");
			server.login(client);
			
			updateUsers();
			fillDiscution();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ERROR, we wouldn't connect....");
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public void updateUsers() {
		// Cree les dosiers de bacup
		Manager.Mkdir("Chat");
		Manager.Mkdir("Chat/"+login.getText().toString());
		try {
			listClient = server.getConnected();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (listClient != null) {
			indexClient = new HashMap<>();
			tabbeClient.removeAll();
			for (int i = 0; i < listClient.size(); i++) {
				try {
					if (listClient.get(i) != null) {
						ChatClientInt clientInt = (ChatClientInt) listClient.get(i);
						indexClient.put(clientInt.getName(), i);
						
						Manager.writeFtile("Chat/"+login.getText()+"/"+clientInt.getName(), "");
						
						ChatPan cp = new ChatPan(client,clientInt);
						tabbeClient.addTab(clientInt.getName(), Manager.getImg("img/Icons/arp.png", 30, 30), cp);
						
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);// clear and repaint
		g.drawImage(Manager.getImg("img/background11.jpg"), 0, 0, getWidth(), getHeight(), this);
		g.drawImage(Manager.getImg("img/login.png"), (int) getWidth() / 2 - 90, 70, 180, 180, this);
	}

}
