package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Exit extends JDialog {
	private static final long serialVersionUID = 1L;

	JButton noButton, yesButton, exitBarButton;

	public Exit() {
		setTitle("Exit");
		setUndecorated(true);
		setPreferredSize(new Dimension(700, 250));
		setSize(getPreferredSize());
		setBackground(new Color(0, 0, 0, 0));
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());

		JLabel txt = new JLabel("  Do you really want to exit ?         ");
		txt.setFont(new Font("World of Water", Font.HANGING_BASELINE, 45));
		txt.setForeground(Color.black);
		txt.setLocation(0, 30);

		noButton = new JButton(Manager.getImg("img/Icons/no.png", 100, 100));
		noButton.setBackground(null);
		noButton.setFocusable(true);

		yesButton = new JButton(Manager.getImg("img/Icons/yes.png", 100, 100));
		yesButton.setBackground(null);
		yesButton.setFocusable(true);

		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					if (DiscussionPan.server != null && DiscussionPan.client != null)
						DiscussionPan.server.Exit(DiscussionPan.client);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(650, 200));
		panel.setSize(panel.getPreferredSize());
		panel.setBackground(Color.darkGray);
		panel.setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));// gray));
		panel.add(txt);
		panel.add(noButton);
		panel.add(yesButton);

		exitBarButton = new JButton(" ");
		exitBarButton.setIcon(new ImageIcon(Manager.getImg("img/cancel.png")));
		exitBarButton.setBackground(null);
		exitBarButton.setBorder(null);
		exitBarButton.setFocusable(false);
		exitBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				revalidate();
			}
		});

		JLabel label = new JLabel("   Exit");
		label.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		label.setForeground(Color.WHITE);
		label.setBackground(null);
		label.setOpaque(false);

		JPanel barPanel = new JPanel();
		barPanel.setPreferredSize(new Dimension(700, 40));
		barPanel.setSize(barPanel.getPreferredSize());
		barPanel.setLayout(new BorderLayout());
		barPanel.add(exitBarButton, BorderLayout.EAST);// WEST
		barPanel.add(label, BorderLayout.WEST);
		barPanel.setBackground(new Color(18, 30, 49));// 18,30,49//54, 64, 69
		barPanel.setBorder(BorderFactory.createBevelBorder(0, new Color(18, 30, 49), Color.black));

		add(barPanel);
		add(panel);

		validate();
		setVisible(false);
	}
}
