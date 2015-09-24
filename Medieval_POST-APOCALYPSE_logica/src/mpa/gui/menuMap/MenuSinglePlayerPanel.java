package mpa.gui.menuMap;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import mpa.core.ai.DifficultyLevel;
import mpa.core.logic.GameManager;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.World;
import mpa.core.logic.WorldFromMapInfo;
import mpa.core.logic.WorldLoader;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;

import org.jdom2.JDOMException;

import com.jme3.system.AppSettings;

public class MenuSinglePlayerPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapInfo mapInfo;
	private JPanel mapListPanel;
	private JPanel mapPreview;
	private JPanel inputNamePanel;
	private JPanel difficultyPanel;
	private WorldLoader worldLoader = new WorldLoader();
	private Pair<Float, Float> selectedHQ = null;
	private String playerName = null;
	private JLabel buttonPlay;
	private Image backgroundImage;
	private int selectedHQIndex = 0;

	private DifficultyLevel difficultyLevelSelected = null;
	private String textImagePath = GameProperties.getInstance().getPath("TextImagePath");

	private JLabel backButton = null;
	private JFrame mainFrame;
	private JPanel mainMenuPanel;
	private int xPanel;
	private int yPanel;
	private int widthPanel;
	private int heightPanel;
	private BufferedImage panelBackgroudImage;

	public MenuSinglePlayerPanel()
	{
		// TODO Stub di costruttore generato automaticamente
	}

	public MenuSinglePlayerPanel(JPanel mainMenuPanel, JFrame mainFrame)
	{

		this.mainMenuPanel = mainMenuPanel;
		this.mainFrame = mainFrame;
		this.setSize(mainFrame.getWidth(), mainFrame.getHeight());
		this.setLocation(0, 0);
		this.setLayout(null);

		xPanel = this.getWidth() * 15 / 100;
		yPanel = this.getHeight() * 10 / 100;
		widthPanel = this.getWidth() * 70 / 100;
		heightPanel = this.getHeight() * 80 / 100;

		mapPreview = new MapPreview(this);
		addMapPreviewPanel();

		mapListPanel = new MapListPanel(this);
		addMapListPanel();

		inputNamePanel = new InputNamePanel();
		addInputNamePanel();

		difficultyPanel = new DifficultyPanel(this);
		addDifficultyPanel();

		addBackButton();

		createPlayButton();
		try
		{
			backgroundImage = ImageIO.read(new File(GameProperties.getInstance().getPath("BackgroundImagesPath") + "/background.jpg"));
			panelBackgroudImage = ImageIO.read(new File(GameProperties.getInstance().getPath("BackgroundImagesPath") + "/background1.jpg"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		this.setVisible(true);
	}

	protected void createPlayButton()
	{
		Image imagePlay = null;
		try
		{
			imagePlay = ImageIO.read(new File(textImagePath + "/Play.png"));
			buttonPlay = new JLabel(new ImageIcon(imagePlay));
		} catch (IOException e2)
		{
			// TODO Blocco catch generato automaticamente
			e2.printStackTrace();
		}
		buttonPlay.setBounds(xPanel + widthPanel - imagePlay.getWidth(this) - imagePlay.getWidth(this) / 2, yPanel + heightPanel * 90 / 100,
				imagePlay.getWidth(this), imagePlay.getHeight(this));

		addPlayButtonListener();

		this.add(buttonPlay);
	}

	protected void addPlayButtonListener()
	{
		buttonPlay.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				playerName = ((InputNamePanel) inputNamePanel).getPlayerName();
				if (playerName == null || playerName.equals(""))
				{
					setButtonsStyle();
					JOptionPane.showMessageDialog(new Frame(), "Inserisci il nome che preferisci", "Message", JOptionPane.PLAIN_MESSAGE);
				}
				else if (selectedHQ == null)
					JOptionPane.showMessageDialog(new Frame(), "Seleziona il quartier generale", "Message", JOptionPane.PLAIN_MESSAGE);
				else if (difficultyLevelSelected == null)
					JOptionPane.showMessageDialog(new Frame(), "Seleziona la difficoltà", "Message", JOptionPane.PLAIN_MESSAGE);
				else
				{
					World world = MenuSinglePlayerPanel.this.worldLoader.loadWorld(new WorldFromMapInfo());
					GameManager.init(world, difficultyLevelSelected, false);
					MenuSinglePlayerPanel.this.selectedHQIndex = MenuSinglePlayerPanel.this.worldLoader.makePlayers(
							MenuSinglePlayerPanel.this.playerName, world, MenuSinglePlayerPanel.this.selectedHQ);

					Thread t = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							mpa.gui.gameGui.playingGUI.GameGui app = new mpa.gui.gameGui.playingGUI.GameGui(playerName, null);
							AppSettings gameSettings = new AppSettings(false);
							// gameSettings.setResolution( 800, 600 );
							// GraphicsDevice device = GraphicsEnvironment
							// .getLocalGraphicsEnvironment().getDefaultScreenDevice();

							// gameSettings.setResolution( screenWidth, screenHeight );
							// gameSettings.setFullscreen( true );

							gameSettings.setResolution(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit
									.getDefaultToolkit().getScreenSize().height);
							gameSettings.setFullscreen(true);
							gameSettings.setVSync(false);
							gameSettings.setTitle("Stellar Conquest");
							gameSettings.setUseInput(true);
							gameSettings.setFrameRate(500);
							gameSettings.setSamples(0);
							gameSettings.setRenderer("LWJGL-OpenGL2");
							app.setSettings(gameSettings);
							app.setShowSettings(false);

							// disable statistics
							app.setDisplayFps(false);
							app.setDisplayStatView(false);
							app.start();
						}
					});
					t.start();
				}
			}

		});
	}

	protected void addBackButton()
	{
		Image imageBack = null;
		try
		{
			imageBack = ImageIO.read(new File(textImagePath + "/back.png"));
			backButton = new JLabel(new ImageIcon(imageBack));
		} catch (IOException e2)
		{
			e2.printStackTrace();
		}
		backButton.setBounds(xPanel + imageBack.getWidth(this), yPanel + heightPanel * 90 / 100, imageBack.getWidth(this), imageBack.getHeight(this));
		backButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{

				mainFrame.setContentPane(mainMenuPanel);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				mainMenuPanel.repaint();
				mainFrame.setVisible(true);

			}
		});
		this.add(backButton);

	}

	protected void setButtonsStyle()
	{

		Font font = null;
		try
		{
			font = Font.createFont(Font.PLAIN, new FileInputStream(GameProperties.getInstance().getPath("fontsPath") + "/KELMSCOT.TTF"));
		} catch (FontFormatException | IOException e)
		{
			e.printStackTrace();
		}
		font = font.deriveFont(Font.PLAIN, 15);
		UIManager.put("Label.font", font);
		UIManager.put("Button.font", new Font("URW Chancery L", Font.BOLD, 13));
	}

	public void setMap(String mapName)
	{
		mapPreview.removeAll();
		try
		{
			worldLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		} catch (JDOMException | IOException e)
		{
			JOptionPane.showMessageDialog(this, "Sorry but this map seems to be lost or damaged :(", "MAP ERROR", JOptionPane.ERROR_MESSAGE);

			// TODO rimuovere la mappa danneggiata (o persa) dalla lista!!!!!
		}
		mapInfo = worldLoader.getMapInfo();
		((MapPreview) mapPreview).setMapDimension(mapInfo.getWidth(), mapInfo.getHeight());
		((MapPreview) mapPreview).loadMap(mapInfo);
		selectedHQ = null;
		this.repaint();
	}

	public void setSelectedHeadQuarter(Pair<Float, Float> headQuarterPosition)
	{
		if (mapInfo.getHeadQuarters().contains(headQuarterPosition))
			selectedHQ = headQuarterPosition;
	}

	public Pair<Float, Float> getSelectedHQ()
	{
		return selectedHQ;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel)
	{
		this.difficultyLevelSelected = difficultyLevel;
	}

	protected void addMapPreviewPanel()
	{
		mapPreview.setBounds(xPanel + widthPanel * 55 / 100, yPanel + 30, widthPanel * 45 / 100 - 30, heightPanel * 55 / 100);
		mapPreview.repaint();
		this.add(mapPreview);
	}

	protected void addMapListPanel()
	{
		mapListPanel.setBounds(xPanel + widthPanel * 80 / 100, yPanel + 50 + heightPanel * 60 / 100, widthPanel * 19 / 100 + 5,
				heightPanel * 20 / 100);
		System.out.println(mapListPanel.getBounds());
		this.add(mapListPanel);
	}

	protected void addInputNamePanel()
	{
		inputNamePanel.setBounds(xPanel + 40, yPanel + 50, widthPanel * 50 / 100, heightPanel * 30 / 100);
		inputNamePanel.setOpaque(false);
		this.add(inputNamePanel);
	}

	protected void addDifficultyPanel()
	{
		difficultyPanel.setBounds(xPanel + 40, yPanel + heightPanel * 60 / 100, widthPanel * 45 / 100, heightPanel * 25 / 100);
		this.repaint();
		this.add(difficultyPanel);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(backgroundImage, getX(), getY(), getWidth(), getHeight(), this);
		g.drawImage(panelBackgroudImage, xPanel, yPanel, widthPanel, heightPanel, this);
	}

}
