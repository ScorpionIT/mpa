package mpa.gui.menuMap;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mpa.core.logic.GameManager;
import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.MapInfo;
import mpa.core.logic.WorldLoader;
import mpa.core.logic.Pair;
import mpa.core.logic.World;
import mpa.core.logic.WorldFromMapInfo;

public class MainMenuPanel extends JPanel
{

	private MapInfo mapInfo;
	private MapListPanel mapListPanel;
	private MapPreview mapPreview = new MapPreview(this);
	private InputNamePanel inputNamePanel;
	private WorldLoader worldLoader = new WorldLoader();

	private Pair<Float, Float> selectedHQ = null;
	private String playerName = null;
	private Button button;

	public MainMenuPanel()
	{
		this.setBackground(Color.yellow);
		mapListPanel = new MapListPanel(this);
		inputNamePanel = new InputNamePanel(this);

		this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		this.setLayout(null);

		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		mapPreview.setBounds(screenWidth * 55 / 100, 30, screenWidth * 45 / 100 - 30, screenHeight * 55 / 100);
		this.add(mapPreview);

		mapListPanel.setBounds(screenWidth * 80 / 100, 50 + screenHeight * 60 / 100, screenWidth * 19 / 100 + 5, screenHeight * 20 / 100);
		// System.out.println(mapListPanel.getBounds());
		this.add(mapListPanel);

		inputNamePanel.setBounds(40, 45, screenWidth * 50 / 100, screenHeight * 85 / 100);
		this.add(inputNamePanel);
		mapListPanel.setOpaque(false);
		mapListPanel.repaint();

		button = new Button("Play");
		button.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (playerName == null)
				{
					JOptionPane.showMessageDialog(new Frame(), "Inserisci il nome che preferisci", "", JOptionPane.PLAIN_MESSAGE);
				}
				else if (selectedHQ == null)
					JOptionPane.showMessageDialog(new Frame(), "Seleziona il quartier generale", "", JOptionPane.PLAIN_MESSAGE);
				else
				{
					World world = MainMenuPanel.this.worldLoader.loadWorld(new WorldFromMapInfo());
					GameManager.init(world,
							MainMenuPanel.this.worldLoader.createSinglePlayer(MainMenuPanel.this.playerName, world, MainMenuPanel.this.selectedHQ));
				}

			}
		});
		button.setBounds(this.getWidth() - 190, this.getHeight() - 100, 139, 30);
		this.add(button);

		this.setVisible(true);
	}

	public void setMap(String mapName)
	{

		worldLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		mapInfo = worldLoader.getMapInfo();
		mapPreview.setMapDimension(mapInfo.getWidth(), mapInfo.getHeight());
		mapPreview.loadMap(mapInfo);

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

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setLocation(0, 0);
		frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		frame.setContentPane(new MainMenuPanel());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;

	}
}
