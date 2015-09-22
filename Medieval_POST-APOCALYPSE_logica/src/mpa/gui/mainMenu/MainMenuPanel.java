package mpa.gui.mainMenu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.core.util.GameProperties;
import mpa.gui.mapEditor.MainMapEditorPanel;
import mpa.gui.menuMap.MenuSinglePlayerPanel;

public class MainMenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Image backgroundImage;
	private JLabel singlePlayer;
	private JLabel multiPlayer;
	private JLabel createServer;
	private JLabel mapEditor;
	private JLabel options;
	private JLabel quit;
	private String textImagePath = GameProperties.getInstance().getPath("TextImagePath");

	public MainMenuPanel(int x, int y, int width, int height, final JFrame mainFrame)
	{
		setBounds(x, y, width, height);

		int yIncrement = (this.getHeight() - this.getHeight() * 60 / 100) / 6;
		int yComponent = this.getHeight() / 4;
		int xComponent;
		setLayout(null);
		try
		{
			backgroundImage = ImageIO.read(new File("Assets/BackgroundImages/backgroundMainPanel.jpg"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		ImageIcon imageIcon = null;

		imageIcon = loadImageIcon("SinglePlayer.png");
		singlePlayer = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		singlePlayer.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		singlePlayer.setVisible(true);
		singlePlayer.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				final JPanel menuSinglePlayerPanel = new MenuSinglePlayerPanel(mainFrame.getWidth() * 15 / 100, mainFrame.getHeight() * 10 / 100,
						mainFrame.getWidth() * 70 / 100, mainFrame.getHeight() * 80 / 100, MainMenuPanel.this, mainFrame);

				JPanel panelConponent = new JPanel()
				{

					@Override
					protected void paintComponent(Graphics g)
					{

						this.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
						super.paintComponent(g);
						Image background = null;
						try
						{
							background = ImageIO.read(new File("Assets/BackgroundImages/background.jpg"));
						} catch (IOException e)
						{
							// TODO Blocco catch generato automaticamente
							e.printStackTrace();
						}
						g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
						this.add(menuSinglePlayerPanel);
					}
				};

				mainFrame.getContentPane().removeAll();
				mainFrame.getContentPane().add(panelConponent);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				menuSinglePlayerPanel.repaint();
				mainFrame.setVisible(true);
			}
		});
		add(singlePlayer);

		yComponent += yIncrement;

		imageIcon = loadImageIcon("Multiplayer.png");
		multiPlayer = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		multiPlayer.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		multiPlayer.setVisible(true);
		multiPlayer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				JPanel serverSelectionPanel = new mpa.gui.multiplayer.ServerSelectionPanel(mainFrame, MainMenuPanel.this);

				mainFrame.getContentPane().removeAll();
				mainFrame.getContentPane().add(serverSelectionPanel);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				serverSelectionPanel.repaint();
				mainFrame.setVisible(true);

			}
		});
		add(multiPlayer);

		yComponent += yIncrement;

		imageIcon = loadImageIcon("CreateServer.png");
		createServer = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		createServer.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		createServer.setVisible(true);

		createServer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{

			}
		});
		add(createServer);

		yComponent += yIncrement;

		imageIcon = loadImageIcon("MapEditor.png");
		mapEditor = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		mapEditor.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		mapEditor.setVisible(true);
		mapEditor.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				MainMapEditorPanel mainMapEditorPanel = new MainMapEditorPanel(MainMenuPanel.this, mainFrame);
				mainFrame.getContentPane().add(mainMapEditorPanel);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				mainMapEditorPanel.repaint();
			}
		});
		add(mapEditor);

		yComponent += yIncrement;

		imageIcon = loadImageIcon("Options.png");
		options = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		options.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		options.setVisible(true);
		add(options);

		yComponent += yIncrement;

		imageIcon = loadImageIcon("Quit.png");
		quit = new JLabel(imageIcon);
		xComponent = this.getWidth() * 50 / 100 - imageIcon.getIconWidth() / 2;
		quit.setBounds(xComponent, yComponent, imageIcon.getIconWidth(), yIncrement);
		quit.setVisible(true);
		quit.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				System.exit(0);
			}
		});
		add(quit);

		setVisible(true);
	}

	private ImageIcon loadImageIcon(String fileName)
	{

		Image imageText = null;
		try
		{
			imageText = ImageIO.read(new File(textImagePath + "/" + fileName));
			// imageText = imageText.getScaledInstance(imageText.getWidth(this) * percentuage / 100,
			// imageText.getHeight(this) * percentuage / 100,
			// Image.SCALE_FAST);

		} catch (IOException e)
		{
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
		return new ImageIcon(imageText);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}

	public static void main(String[] args)
	{
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		JFrame frame = new JFrame();
		frame.setLocation(0, 0);

		frame.setSize(screenWidth, screenHeight);

		MainMenuPanel mainMenuPanel = new MainMenuPanel(0, 0, screenWidth, screenHeight, frame);
		frame.add(mainMenuPanel);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
