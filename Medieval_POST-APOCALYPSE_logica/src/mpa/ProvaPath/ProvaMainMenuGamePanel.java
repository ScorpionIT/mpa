package mpa.ProvaPath;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import mpa.core.ai.DifficultyLevel;
import mpa.core.logic.GameManager;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.World;
import mpa.core.logic.WorldFromMapInfo;
import mpa.core.logic.WorldLoader;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;
import mpa.gui.menuMap.DifficultyPanel;
import mpa.gui.menuMap.InputNamePanel;
import mpa.gui.menuMap.MapListPanel;
import mpa.gui.menuMap.MapPreview;
import mpa.gui.menuMap.MenuSinglePlayerPanel;

import org.jdom2.JDOMException;

public class ProvaMainMenuGamePanel extends MenuSinglePlayerPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapInfo mapInfo;
	private MapListPanel mapListPanel;
	private MapPreview mapPreview;
	private InputNamePanel inputNamePanel;
	private DifficultyPanel difficultyPanel;
	private WorldLoader worldLoader = new WorldLoader();
	private Pair<Float, Float> selectedHQ = null;
	private String playerName = null;
	private JLabel buttonPlay;
	private Image backgroundImage;
	private int selectedHQIndex = 0;
	int screenWidth;
	int screenHeight;
	DifficultyLevel difficultyLevelSelected = null;
	private String textImagePath = GameProperties.getInstance().getPath("TextImagePath");

	public ProvaMainMenuGamePanel(int x, int y, int width, int height)
	{

		this.setSize(width, height);
		this.setLocation(x, y);
		this.setLayout(null);
		screenWidth = this.getWidth();
		screenHeight = this.getHeight();

		addMapPreviewPanel();

		addMapListPanel();

		addInputNamePanel();

		addDifficultyPanel();
		System.out.println(textImagePath);

		try
		{
			Image imagePlay = ImageIO.read(new File(textImagePath + "/Play.png"));
			imagePlay = imagePlay.getScaledInstance(screenWidth * 20 / this.getWidth() * 10, 40, Image.SCALE_FAST);
			buttonPlay = new JLabel(new ImageIcon(imagePlay));
		} catch (IOException e2)
		{
			// TODO Blocco catch generato automaticamente
			e2.printStackTrace();
		}
		buttonPlay.setBounds(this.getWidth() - this.getWidth() * 15 / 100, 52 + screenHeight * 80 / 100, screenWidth * 20 / this.getWidth() * 10, 40);
		buttonPlay.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				playerName = inputNamePanel.getPlayerName();
				if (playerName == null || playerName.equals(""))
				{
					JOptionPane.showMessageDialog(new Frame(), "Inserisci il nome che preferisci", "", JOptionPane.PLAIN_MESSAGE);
				}
				else if (selectedHQ == null)
					JOptionPane.showMessageDialog(new Frame(), "Seleziona il quartier generale", "", JOptionPane.PLAIN_MESSAGE);
				else if (difficultyLevelSelected == null)
					JOptionPane.showMessageDialog(new Frame(), "Seleziona la difficoltà", "", JOptionPane.PLAIN_MESSAGE);
				else
				{
					World world = ProvaMainMenuGamePanel.this.worldLoader.loadWorld(new WorldFromMapInfo());
					GameManager.init(world, difficultyLevelSelected, false);
					ProvaMainMenuGamePanel.this.selectedHQIndex = ProvaMainMenuGamePanel.this.worldLoader.makePlayers(
							ProvaMainMenuGamePanel.this.playerName, world, ProvaMainMenuGamePanel.this.selectedHQ);

					int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
					int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

					JFrame frame = new JFrame();
					frame.setLocation(0, 0);

					frame.setSize(1000, 1000);
					frame.setLocation((screenWidth - 1000) / 2, 0);
					ProvaPath provaPath = new ProvaPath(playerName);
					provaPath.setBounds(0, 0, 1000, 1000);

					frame.setContentPane(new Panel());
					frame.getContentPane().setBackground(Color.BLACK);
					frame.getContentPane().add(provaPath);
					frame.getContentPane().setLayout(null);
					frame.getContentPane().setVisible(true);;
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				}
			}
		});

		this.add(buttonPlay);
		try
		{
			backgroundImage = ImageIO.read(new File(GameProperties.getInstance().getPath("BackgroundImagesPath") + "/mainMenu.jpg"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		this.setVisible(true);
	}

	public void setMap(String mapName)
	{
		mapPreview.removeAll();
		try
		{
			worldLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		} catch (JDOMException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapInfo = worldLoader.getMapInfo();
		mapPreview.setMapDimension(mapInfo.getWidth(), mapInfo.getHeight());
		mapPreview.loadMap(mapInfo);
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
		mapPreview = new MapPreview(this);
		mapPreview.setBounds(screenWidth * 55 / 100, 30, screenWidth * 45 / 100 - 30, screenHeight * 55 / 100);
		mapPreview.repaint();
		this.add(mapPreview);
	}

	protected void addMapListPanel()
	{
		mapListPanel = new MapListPanel(this);
		mapListPanel.setBounds(screenWidth * 80 / 100, 50 + screenHeight * 60 / 100, screenWidth * 19 / 100 + 5, screenHeight * 20 / 100);
		System.out.println(mapListPanel.getBounds());
		this.add(mapListPanel);
	}

	protected void addInputNamePanel()
	{
		inputNamePanel = new InputNamePanel();
		inputNamePanel.setBounds(40, 45, screenWidth * 50 / 100, screenHeight * 40 / 100);
		inputNamePanel.setOpaque(false);
		this.add(inputNamePanel);
	}

	protected void addDifficultyPanel()
	{
		difficultyPanel = new DifficultyPanel(this);
		difficultyPanel.setBounds(40, screenHeight * 60 / 100, screenWidth * 45 / 100, screenHeight * 25 / 100);
		this.repaint();
		this.add(difficultyPanel);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, this);
	}

	public static void main(String[] args)
	{

		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		JFrame frame = new JFrame();
		frame.setLocation(0, 0);

		frame.setSize(screenWidth, screenHeight);
		ProvaMainMenuGamePanel mainMenuGamePanel = new ProvaMainMenuGamePanel(screenWidth * 15 / 100, screenHeight * 10 / 100,
				screenWidth * 70 / 100, screenHeight * 80 / 100);

		frame.setContentPane(new Panel());
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().add(mainMenuGamePanel);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setVisible(true);;
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
