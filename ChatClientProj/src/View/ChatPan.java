package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import RMI.ChatClient;
import RMI.ChatClientInt;

public class ChatPan extends JPanel {
	private static final long serialVersionUID = 1L;
	private ChatClient client;
	private ChatClientInt clientInt;

	public JPanel bar, chat;
	private JScrollPane showScrollPane;
	public JTextField msg;
	private static Image img = Manager.getImg("img/background11.jpg");

	public ChatPan() {
		setSize(1000, 700);
		setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
		setOpaque(true);
		validate();
	};

	public ChatPan(ChatClient client, ChatClientInt clientInt) {
		setSize(1000, 700);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
		setOpaque(true);
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});

		validate();

		this.client = client;
		this.clientInt = clientInt;

		build();
		fillChat();
	}

	private void build() {
		chat = new JPanel();
		chat.setOpaque(false);
		chat.setLayout(new FlowLayout());
		chat.setLayout(new GridLayout(0, 1, 10, 10));

		showScrollPane = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		showScrollPane.setPreferredSize(getSize());
		showScrollPane.setOpaque(false);
		showScrollPane.getViewport().setOpaque(false);
		showScrollPane.validate();

		bar = new JPanel();
		bar.setPreferredSize(new Dimension(getWidth(), 42));
		bar.setSize(bar.getPreferredSize());
		bar.setOpaque(false);
		bar.setLayout(new FlowLayout());

		msg = new JTextField();
		msg.setFont(new Font("World of Water", Font.LAYOUT_NO_START_CONTEXT, 16));
		msg.setPreferredSize(new Dimension(500, 40));
		msg.setSize(msg.getPreferredSize());

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.sendMessage(clientInt, msg.getText());
					Manager.writeMsg(1,"Chat/"+client.getName()+"/"+clientInt.getName(),msg.getText());
					sendMessage(msg.getText(), true);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton sendfile = new JButton("File");
		sendfile.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(new JFrame(), "Chose File");
				fd.show();
				try {
					File file = new File(fd.getDirectory() + "/" + fd.getFile());
					if (file != null) {
//						ChatClientInt c = listClient.get(tabbeClient.getSelectedIndex());
						client.sendFile(clientInt, file);
						sendFile(file, true);
						Manager.writeMsg(3,"Chat/"+client.getName()+"/"+clientInt.getName(),file.getPath());
					}
				} catch (Exception ereur) {
					System.err.println(ereur);
				}
			}
		});

		bar.add(msg);
		bar.add(send);
		bar.add(sendfile);

		add(bar, BorderLayout.SOUTH);
		add(showScrollPane, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	public void fillChat() {
		try {
			Manager.readFile("Chat/"+client.getName()+"/"+clientInt.getName(),this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg, boolean sended) {
//		sended = sended == true ? false : true;
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setOpaque(false);
		text.setFont(new Font("World of Water", Font.TYPE1_FONT, 17));
		text.setLineWrap(true);
		int H = msg.length() / 45;
		if (msg.length() % 45 > 1)
			H++;
		text.setPreferredSize(new Dimension(350, H * 40));
		text.setSize(text.getPreferredSize());
		text.setText(msg);

		Border b = new Border(sended);
		b.setSize(text.getPreferredSize());
		b.repaint();
		b.add(text);

		JPanel continer = new JPanel();
		continer.setOpaque(false);
		if (sended) {
			continer.setLayout(new FlowLayout(2));
			text.setForeground(Color.white);
			this.msg.setText("");
		} else {
			continer.setLayout(new FlowLayout(0));
			text.setForeground(Color.BLACK);
		}
		continer.add(b);
		continer.setPreferredSize(new Dimension(chat.getWidth(), (int) text.getPreferredSize().getHeight()));
		continer.setSize(continer.getPreferredSize());
		chat.add(continer);

		revalidate();
		repaint();
		
		int height = (int)chat.getPreferredSize().getHeight();
		showScrollPane.getVerticalScrollBar().setValue(height+30);
//        Rectangle rect = new Rectangle(0,height,10,10);
//        chat.scrollRectToVisible(rect);
	}

	public void sendFile(File file, boolean sended) {
	
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		final JButton fileButton = new JButton();
		try {
			fileButton.setIcon(fileSystemView.getSystemIcon(file));
			fileButton.setText(fileSystemView.getSystemDisplayName(file));
			fileButton.setToolTipText(file.getPath());
			fileButton.setPreferredSize(new Dimension(300, 40));
			fileButton.setSize(fileButton.getPreferredSize());
			fileButton.setBackground(new Color(0, 0, 0, 0));
			if (sended)
				fileButton.setForeground(Color.white);
			else
				fileButton.setForeground(Color.BLACK);
			fileButton.setOpaque(false);
			fileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File f = new File(fileButton.getToolTipText());
					OpenFile(f);
				}
			});
			;
		} catch (Exception e) {
			e.getStackTrace();
		}

		Border b = new Border(sended);
		b.setSize(fileButton.getPreferredSize());
		b.add(fileButton);

		JPanel continer = new JPanel();
		continer.setOpaque(false);
		if (sended)
			continer.setLayout(new FlowLayout(2));
		else
			continer.setLayout(new FlowLayout(0));

		continer.add(b);
		continer.setPreferredSize(new Dimension(chat.getWidth(), (int) fileButton.getPreferredSize().getHeight()));
		continer.setSize(continer.getPreferredSize());

		chat.add(continer);

		revalidate();
		repaint();
		
		int height = (int)chat.getPreferredSize().getHeight();
		showScrollPane.getVerticalScrollBar().setValue(height+30);
//        Rectangle rect = new Rectangle(0,height,10,10);
//        chat.scrollRectToVisible(rect);
	}

	public void OpenFile(File file) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException ex) {
				System.err.println("!! Ereur : " + ex);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);// clear and repaint
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
	
	public static void setBackImage(Image im) {
		img=im;
	}

	public class Border extends JPanel {
		boolean sended;

		public Border(boolean b) {
			this.sended = b;
			setOpaque(false);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);// clear and repaint
			if (sended)
				g.setColor(new Color(18, 210, 210, 100));
			else
				g.setColor(new Color(255, 255, 255, 120));
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
		}
	}
}