package mpa.gui.mainMenu;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.core.util.GameProperties;

public class MainMenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel title;

	private Image backgroundImage;
	private JLabel singlePlayer;
	private JLabel multiPlayer;
	private JLabel createServer;
	private JLabel mapEditor;
	private JLabel options;
	private JLabel quit;
	private String textImagePath = GameProperties.getInstance().getPath("TextImagePath");

	public MainMenuPanel(int x, int y, int width, int height)
	{
		setBounds(x, y, width, height);

		int yIncrement = (this.getHeight() - this.getHeight() / 2) / 6;
		int yComponent = this.getHeight() / 4;
		setLayout(null);
		try
		{
			backgroundImage = ImageIO.read(new File("Assets/BackgroundImages/mainMenu.jpg"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		title = new JLabel("CI SARÀ UN'IMMAGINE");
		title.setBounds(25 * width / 100, 0, 50 * width / 100, 10 * height / 100);
		title.setVisible(true);
		add(title);

		singlePlayer = loadJLabel("SinglePlayer.png");
		singlePlayer.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		singlePlayer.setVisible(true);
		add(singlePlayer);

		yComponent += yIncrement;

		multiPlayer = loadJLabel("Multiplayer.png");
		multiPlayer.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		multiPlayer.setVisible(true);
		add(multiPlayer);

		yComponent += yIncrement;

		createServer = loadJLabel("CreateServer.png");
		createServer.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		createServer.setVisible(true);
		add(createServer);

		yComponent += yIncrement;

		mapEditor = loadJLabel("MapEditor.png");
		mapEditor.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		mapEditor.setVisible(true);
		add(mapEditor);

		yComponent += yIncrement;

		options = loadJLabel("Options.png");
		options.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		options.setVisible(true);
		add(options);

		yComponent += yIncrement;

		quit = loadJLabel("Quit.png");
		quit.setBounds(5, yComponent, 25 * width / 100, yIncrement);
		quit.setVisible(true);
		add(quit);

		setVisible(true);
	}

	private JLabel loadJLabel(String fileName)
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
		return new JLabel(new ImageIcon(imageText));
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

		MainMenuPanel mp = new MainMenuPanel(0, 0, screenWidth, screenHeight);
		frame.add(mp);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
