package mpa.gui.multiplayer.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mpa.core.ai.DifficultyLevel;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.WorldLoader;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;
import mpa.gui.menuMap.MapListPanel;
import mpa.gui.menuMap.MapPreview;

import org.jdom2.JDOMException;

public class CreateServerMainPanel extends JPanel
{
	private JLabel backButton = new JLabel();
	private JLabel launchButton = new JLabel();
	private MapPreview mapPreview;
	private MapListPanel mapListPanel;
	private JComboBox<String> numberOfBotsBox;
	private int numberOfBots = -1;
	private Image backgroundImage;
	private JFrame mainFrame;
	private WorldLoader worldLoader = new WorldLoader();
	private MapInfo mapInfo = null;
	private WelcomeServer welcomeServer;
	private String map;
	private JPanel parent;
	private BufferedImage panelBackgroudImage;
	private boolean visiblePanel = false;
	private JComboBox<Object> difficultyLevelComboBox;
	private DifficultyLevel difficultyLevelSelected = null;
	private List<DifficultyLevel> difficlutyLevels = Arrays.asList(DifficultyLevel.values());
	private JLabel difficultyLevelLabel;

	public CreateServerMainPanel(int x, int y, int width, int height, final JFrame mainFrame, JPanel parent)
	{
		this.mainFrame = mainFrame;
		this.parent = parent;
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setLayout(null);

		addMapPreview();

		mapListPanel = new MapListPanel(this);
		mapListPanel.setBounds(30 * this.getWidth() / 100, 70 * this.getHeight() / 100, 15 * this.getWidth() / 100, 20 * this.getHeight() / 100);
		this.add(mapListPanel);

		try
		{
			backgroundImage = ImageIO.read(new File(GameProperties.getInstance().getPath("BackgroundImagesPath") + "/background.jpg"));
			panelBackgroudImage = ImageIO.read(new File(GameProperties.getInstance().getPath("BackgroundImagesPath")
					+ "/backgroundHeadquarterPanel.png"));
		} catch (IOException e)
		{
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}

		addBotsComboBox();
		addDifficultyLevelComboBox();
		addBackButton();
		addLauchButton();

		this.setVisible(true);

	}

	private void addDifficultyLevelComboBox()
	{
		difficultyLevelLabel = new JLabel("Select Difficulty Level");
		difficultyLevelLabel.setForeground(Color.WHITE);
		difficultyLevelLabel.setFont(new Font("URW Chancery L", Font.BOLD, 30));

		difficultyLevelLabel.setBounds(60 * this.getWidth() / 100, 80 * this.getHeight() / 100, 30 * this.getWidth() / 100,
				5 * this.getHeight() / 100);

		this.add(difficultyLevelLabel);
		difficultyLevelComboBox = new JComboBox<>();

		difficultyLevelComboBox.addItem("-");
		for (DifficultyLevel difficultyLevel : difficlutyLevels)
		{
			difficultyLevelComboBox.addItem(difficultyLevel.toString());
		}

		difficultyLevelComboBox.setBounds(60 * this.getWidth() / 100, 85 * this.getHeight() / 100, 10 * this.getWidth() / 100,
				2 * this.getHeight() / 100);
		this.add(difficultyLevelComboBox);
	}

	private void addBotsComboBox()
	{
		JLabel numberOfBotsLabel = new JLabel("Select Number of Bots");
		numberOfBotsLabel.setForeground(Color.WHITE);
		numberOfBotsLabel.setFont(new Font("URW Chancery L", Font.BOLD, 30));

		numberOfBotsLabel.setBounds(60 * this.getWidth() / 100, 70 * this.getHeight() / 100, 30 * this.getWidth() / 100, 10 * this.getHeight() / 100);

		this.add(numberOfBotsLabel);
		numberOfBotsBox = new JComboBox<>();

		numberOfBotsBox.setBounds(60 * this.getWidth() / 100, 77 * this.getHeight() / 100, 10 * this.getWidth() / 100, 2 * this.getHeight() / 100);
		this.add(numberOfBotsBox);
	}

	private void addLauchButton()
	{

		Image imageBack = null;
		try
		{
			imageBack = ImageIO.read(new File(GameProperties.getInstance().getPath("textImagePath") + "/Launch.png"));
			launchButton = new JLabel(new ImageIcon(imageBack));
		} catch (IOException e2)
		{
			e2.printStackTrace();
		}
		launchButton.setBounds(85 * this.getWidth() / 100, this.getHeight() * 85 / 100, imageBack.getWidth(this), imageBack.getHeight(this));

		launchButton.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(java.awt.event.MouseEvent e)
			{

				numberOfBots = Integer.parseInt((String) numberOfBotsBox.getSelectedItem());
				if (numberOfBots > 0)
				{
					String selectedItem = (String) difficultyLevelComboBox.getSelectedItem();
					for (DifficultyLevel difficultyLevel : difficlutyLevels)
					{
						if (difficultyLevel.name().equals(selectedItem))
							difficultyLevelSelected = difficultyLevel;
					}
				}

				if (mapInfo == null)
				{
					JOptionPane.showMessageDialog(new Frame(), "Seleziona la mappa", "Message", JOptionPane.PLAIN_MESSAGE);
				}
				else if (numberOfBots == -1)
				{
					JOptionPane.showMessageDialog(new Frame(), "Seleziona il numero di bot", "Message", JOptionPane.PLAIN_MESSAGE);
				}
				else if (numberOfBots > 0 && difficultyLevelSelected == null)
				{
					JOptionPane.showMessageDialog(new Frame(), "Seleziona il livello di difficoltà", "Message", JOptionPane.PLAIN_MESSAGE);
				}
				else
				{
					welcomeServer = new WelcomeServer(mapInfo.getNumberOfPlayers() - numberOfBots, map);
					welcomeServer.start();
					CreateServerMainPanel.this.removeAll();

					JLabel createdServer = new JLabel("Server ");
					createdServer.setForeground(Color.WHITE);
					createdServer.setFont(new Font("URW Chancery L", Font.BOLD, 50));

					createdServer.setBounds(30 * CreateServerMainPanel.this.getWidth() / 100, 30 * CreateServerMainPanel.this.getHeight() / 100,
							30 * CreateServerMainPanel.this.getWidth() / 100, 10 * CreateServerMainPanel.this.getHeight() / 100);

					CreateServerMainPanel.this.add(createdServer);

					JLabel stopServer = null;
					try
					{
						stopServer = new JLabel(new ImageIcon(ImageIO.read(new File(GameProperties.getInstance().getPath("textImagePath")
								+ "/stopServer.png"))));
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
					stopServer.setBounds(45 * CreateServerMainPanel.this.getWidth() / 100, 50 * CreateServerMainPanel.this.getHeight() / 100,
							30 * CreateServerMainPanel.this.getWidth() / 100, 10 * CreateServerMainPanel.this.getHeight() / 100);

					stopServer.addMouseListener(new MouseAdapter()
					{
						public void mouseReleased(MouseEvent e)
						{
						};
					});

					CreateServerMainPanel.this.add(stopServer);
					addBackButton();
					visiblePanel = true;
					CreateServerMainPanel.this.updateUI();

				}
			};
		});

		this.add(launchButton);
	}

	private void addMapPreview()
	{
		mapPreview = new MapPreview(this)
		{
			@Override
			public void loadMap(MapInfo mapInfo)
			{
				this.mapInfo = mapInfo;

				this.headQuartersLabel.clear();

				for (Pair<Float, Float> headQuarterPosition : mapInfo.getHeadQuarters())
				{
					Image image = images.get("headQuarter");
					image = image.getScaledInstance(W(GameProperties.getInstance().getObjectWidth("headQuarter")), H(GameProperties.getInstance()
							.getObjectHeight("headQuarter")), 0);

					ImageIcon imageIcon = new ImageIcon(image);
					JLabel jLabel = new JLabel(imageIcon);
					jLabel.setBounds(graphicX(headQuarterPosition.getFirst()), graphicY(headQuarterPosition.getSecond()), W(GameProperties
							.getInstance().getObjectWidth("headQuarter")), H(GameProperties.getInstance().getObjectHeight("headQuarter")));
					this.add(jLabel);

					headQuartersLabel.put(jLabel, headQuarterPosition);
				}
			}
		};

		mapPreview.setBounds(25 * this.getWidth() / 100, 5 * this.getHeight() / 100, 50 * this.getWidth() / 100, 60 * this.getHeight() / 100);
		this.add(mapPreview);
	}

	public void setMap(String mapName)
	{
		map = mapName;
		mapPreview.removeAll();
		try
		{
			worldLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		} catch (JDOMException | IOException e)
		{
			JOptionPane.showMessageDialog(this, "Sorry but this map seems to be lost or damaged :(", "MAP ERROR", JOptionPane.ERROR_MESSAGE);
		}
		mapInfo = worldLoader.getMapInfo();
		((MapPreview) mapPreview).setMapDimension(mapInfo.getWidth(), mapInfo.getHeight());
		((MapPreview) mapPreview).loadMap(mapInfo);
		this.repaint();

		numberOfBots = 0;
		numberOfBotsBox.removeAllItems();
		for (int i = 0; i <= mapInfo.getNumberOfPlayers() - 2; i++)
		{
			numberOfBotsBox.addItem(String.valueOf(i));
		}

		if (mapInfo.getNumberOfPlayers() - 2 > 0)
		{
			difficultyLevelLabel.setVisible(true);
			difficultyLevelComboBox.setVisible(true);
			difficultyLevelSelected = null;

		}
		else
		{
			difficultyLevelLabel.setVisible(false);
			difficultyLevelComboBox.setVisible(false);

		}
		numberOfBotsBox.repaint();

	}

	@Override
	protected void paintComponent(Graphics g)
	{
		// TODO Stub di metodo generato automaticamente
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		if (visiblePanel)
		{
			int xPanel = this.getWidth() * 20 / 100;
			int yPanel = this.getHeight() * 10 / 100;
			int widthPanel = this.getWidth() * 60 / 100;
			int heightPanel = this.getHeight() * 70 / 100;
			g.drawImage(panelBackgroudImage, xPanel, yPanel, widthPanel, heightPanel, this);
		}
	}

	protected void addBackButton()
	{
		Image imageBack = null;
		try
		{
			imageBack = ImageIO.read(new File(GameProperties.getInstance().getPath("textImagePath") + "/back.png"));
			backButton = new JLabel(new ImageIcon(imageBack));
		} catch (IOException e2)
		{
			e2.printStackTrace();
		}
		backButton.setBounds(10 * this.getWidth() / 100, this.getHeight() * 85 / 100, imageBack.getWidth(this), imageBack.getHeight(this));
		backButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{

				mainFrame.setContentPane(parent);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				parent.repaint();
				mainFrame.setVisible(true);

			}
		});
		this.add(backButton);

	}

}
