package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Option extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	boolean musicIsOn;
	public JLabel sonLabel;
	private JButton BackButton, backButton;
	private JButton exitButton, UndecoratedButton;
	private JPanel panel;
	private Image img, backImage;
	private Boolean backIsChanged;
	private int i = 0, y, dely;
	private Timer timer;

	public Option() {

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

		img = Manager.getImg("img/background4.jpg");
		backIsChanged = false;

		BackButton = new JButton("Change Background",
				Manager.getImg("img/background.png", getWidth() / 13, getHeight() / 13));
		BackButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		BackButton.setBackground(new Color(0, 0, 0, 0));
		BackButton.setFocusable(false);
		BackButton.setOpaque(false);
		BackButton.addActionListener(this);

		exitButton = new JButton(Manager.getImg("img/exit1.png", getWidth() / 13, getHeight() / 13));
		exitButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		exitButton.setBackground(new Color(0, 0, 0, 0));
		exitButton.setFocusable(false);
		exitButton.setOpaque(false);
		exitButton.addActionListener(this);

		backButton = new JButton(Manager.getImg("img/Icons/refresh.png", getWidth() / 13, getHeight() / 13));
		backButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		backButton.setBackground(new Color(0, 0, 0, 0));
		backButton.setFocusable(false);
		backButton.setOpaque(false);
		backButton.addActionListener(this);

		UndecoratedButton = new JButton("Undecorated Frame",
				Manager.getImg("img/Icons/undecorated0.png", getWidth() / 13, getHeight() / 13));
		UndecoratedButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		UndecoratedButton.setForeground(Color.WHITE);
		UndecoratedButton.setBackground(new Color(0, 0, 0, 0));
		UndecoratedButton.setFocusable(false);
		UndecoratedButton.setOpaque(false);
		UndecoratedButton.addActionListener(this);

		panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(500, 650));
		panel.add(BackButton);
		panel.add(backButton);
		panel.add(exitButton);
		panel.add(UndecoratedButton);

		JPanel pad = new JPanel();
		pad.setBackground(null);
		pad.setPreferredSize(new Dimension(this.getWidth(), 200));
		pad.setOpaque(false);
		add(pad, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		validate();
	}

	public JButton getUndecoratedButton() {
		return UndecoratedButton;
	}

	public void efait() {
		backIsChanged = true;
		y = -100;
		dely = 0;

		timer = new Timer(4, null);
		timer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (y < 350)
					y++;
				else {
					if (dely < 700)
						dely++;
					else {
						backIsChanged = false;
						timer.stop();
					}
				}
				repaint();
			}
		});
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);// clear and repaint
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		draw(g);
	}

	public void draw(Graphics g) {
		if (backIsChanged) {

			g.setColor(Color.black);
			g.drawLine(getWidth() * 6 / 10, 250, getWidth() * 7 / 10 - 2, y - 6 - 2);
			g.drawLine(getWidth() * 6 / 10, 250, getWidth() * 7 / 10 - 2, y + 30 + 210 + 2);
			g.drawLine(getWidth() * 7 / 10, y + 6, getWidth() * 7 / 10 + 260, y + 6);

			g.drawRect(getWidth() * 7 / 10 - 2, y + 30 - 2, 300 + 4, 210 + 4);
			g.drawRect(getWidth() * 7 / 10 - 1, y + 30 - 1, 300 + 2, 210 + 2);
			g.drawRect(getWidth() * 7 / 10, y + 30, 300, 210);

			g.setColor(Color.white);

			g.setFont(new Font("Tahoma", Font.BOLD, 20));
			g.drawString("Background is changed !!", getWidth() * 7 / 10, y);

			g.drawImage(backImage, getWidth() * 7 / 10, 30 + y, 300, 210, this);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			if (i < 16)
				i++;
			else
				i = 0;
			backImage = Manager.getImg("img/background" + i + ".jpg");
			ChatPan.setBackImage(backImage);
			efait();
			repaint();
		}

		if (e.getSource() == BackButton) {
			FileDialog fd = new FileDialog(new JFrame(), "son/Techno-Tetris(Remix).wav");
			// afficher le FileChooser...
			fd.show();
			try {
				// assigner à notre fichier de départ, qui était "null" jusqu'à présent, un
				// fichier réel
				File file = new File(fd.getDirectory() + "/" + fd.getFile());
				// si l'assignation a bien fonctionné, le fichier n'est plus "null", donc:
				if (file != null) {
					// ouvrir notre flux d'entrée sur ce fichier
					backImage = new ImageIcon(file.getAbsolutePath()).getImage();
					ChatPan.setBackImage(backImage);
				}
			} catch (Exception ereur) {
				System.err.println(ereur);
			}
			efait();
			repaint();
		}

		if (e.getSource() == exitButton) {
			if (View.exit == null)
				View.exit = new Exit();
			View.exit.setVisible(true);
		}
	}
}
