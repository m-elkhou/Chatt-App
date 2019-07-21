package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel inter;
	public static DiscussionPan discussionPan= new DiscussionPan();
	public static About aboutTP;
	static JTabbedPane tabbePane = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JPanel barPanel;
	public static Exit exit;
	public Option optionTP;

	public Boolean maximized;
	public JButton maximizer;
	public JButton exitButton;
	public JButton ReduireButton;
	public int xx, xy;

	public View() {
		setName("ChatClient");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1120, 700));
		setSize(getPreferredSize());
		setLocationRelativeTo(null);
		setUndecorated(true);
		setResizable(true);
		setIconImage(Manager.getImg("img/Icons/chat5.png", 650, 650).getImage());

		build();

		add(barPanel, BorderLayout.NORTH);
		add(inter, BorderLayout.CENTER);
		setFocusable(true);
		validate();
		setVisible(true);
	}

	private void build() {
		FillBar();
		FillPanel();
		FillMain();
	}
	
	private void FillBar() {
		barPanel = new JPanel();
		inter = new JPanel();
		barPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				barPanelMouseDragged(evt);
			}
		});
		barPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				barPanelMousePressed(evt);
			}
		});

		exitButton = new JButton(" ");
		exitButton.setIcon(new ImageIcon(Manager.getImg("img/cancel.png")));
		exitButton.setBackground(null);
		exitButton.setBorder(null);
		exitButton.setFocusable(false);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exit = new Exit();
				exit.setVisible(true);
			}
		});

		maximizer = new JButton(" ");
		maximizer.setBackground(null);
		maximizer.setBorder(null);
		maximizer.setFocusable(false);
		maximized = false;
		maximizer.setIcon(new ImageIcon(Manager.getImg("img/maximize.png")));
		maximizer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				maximizerMouseClicked(evt);
			}
		});

		ReduireButton = new JButton(" ");
		ReduireButton.setBackground(null);
		ReduireButton.setBorder(null);
		ReduireButton.setFocusable(false);
		ReduireButton.setIcon(new ImageIcon(Manager.getImg("img/upload.png")));
		ReduireButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				Undecorated();
			}
		});

		JPanel barPanelButton = new JPanel();
		barPanelButton.add(ReduireButton);
		barPanelButton.add(maximizer);
		barPanelButton.add(exitButton);
		barPanelButton.setBackground(null);

		JLabel iconLabel = new JLabel("     Chat", Manager.getImg("img/Icons/chat5.png", 30, 25), 2);
		iconLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
		iconLabel.setForeground(Color.WHITE);
		iconLabel.setBackground(null);
		iconLabel.setOpaque(false);

		JLabel esp = new JLabel("            ", 2);
		barPanel.setLayout(new BorderLayout());
		barPanel.add(barPanelButton, BorderLayout.EAST);// WEST
		barPanel.add(esp, BorderLayout.WEST);
		barPanel.add(iconLabel);
		barPanel.setBackground(new Color(18, 30, 49));// 18,30,49//54, 64, 69
		// barPanel.setFocusable(false);
		barPanel.validate();
	}

	private void FillPanel() {
		aboutTP = new About();
		optionTP = new Option();
		inter.setLayout(new BorderLayout());

		tabbePane.addTab("    Home  ", Manager.getImg("img/Icons/home1.png", 30, 25), discussionPan);// "../img/home1.png"
		tabbePane.addTab("  Option  ", Manager.getImg("img/Icons/option.png", 30, 25), optionTP);// "../img/option.png"
		tabbePane.addTab("About-us", Manager.getImg("img/Icons/about-us2.png", 30, 35), aboutTP);// "../img/Icons/about-us.jpg"
																									// //
																									// "../img/Icons/about.png"
		tabbePane.setSelectedIndex(0);
		tabbePane.setFont(new Font("Tahoma", Font.BOLD, 16));// setFont(new Font("Segoe UI Semilight", 5, 15));
		tabbePane.setBackground(new Color(24, 43, 53));// 72, 86, 98
		tabbePane.setForeground(Color.black);
		tabbePane.setFocusable(true);
		tabbePane.validate();

		inter.add(tabbePane);
		inter.setBackground(new Color(18, 33, 43));// 54, 63, 73
		inter.setFocusable(true);
		inter.validate();
	}
	
	private void FillMain() {	
		
	}
	
	public void openGame() {
		inter.removeAll();
//		inter.add(gamePanel, BorderLayout.CENTER);
		inter.revalidate();
		inter.repaint();
	}

	private void Undecorated() {
		dispose();
		if (this.isUndecorated())
			this.setUndecorated(false);
		else
			this.setUndecorated(true);
		pack();
		revalidate();
		repaint();
		setVisible(true);
	}

	private void maximizerMouseClicked(MouseEvent evt) {
		if (!maximized) {
			// handle fullscreen - taskbar
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			setMaximizedBounds(env.getMaximumWindowBounds());
			ImageIcon ii = new ImageIcon(Manager.getImg("img/minimize.png"));
			maximizer.setIcon(ii);
		} else {
			setExtendedState(JFrame.NORMAL);
			ImageIcon ii = new ImageIcon(Manager.getImg("img/maximize.png"));
			maximizer.setIcon(ii);
		}
		maximized = maximized == true ? false : true;
	}

	private void barPanelMouseDragged(MouseEvent evt) {
		int x = evt.getXOnScreen();
		int y = evt.getYOnScreen();
		this.setLocation(x - xx, y - xy);
	}

	private void barPanelMousePressed(MouseEvent evt) {
		xx = evt.getX();
		xy = evt.getY();
	}

	public void setBarButton() {
		exitButton.setIcon(Manager.getImg("img/Icons/exit.png", 30, 30));
		ReduireButton.setIcon(Manager.getImg("img/Icons/reduir.png", 30, 30));
	}

	public static void main(String[] arg) {
		new View();
	}
}
