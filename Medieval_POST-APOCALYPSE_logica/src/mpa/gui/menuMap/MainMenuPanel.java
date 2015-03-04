package mpa.gui.menuMap;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.MapInfo;
import mpa.core.logic.MapLoader;
import mpa.core.logic.Pair;

public class MainMenuPanel extends JPanel
{

	private MapInfo mapInfo;
	private MapListPanel mapListPanel;
	private MapPreview mapPreview = new MapPreview(this);

	private Pair<Float, Float> selectedHQ = null;

	public MainMenuPanel()
	{
		this.setBackground(Color.yellow);
		mapListPanel = new MapListPanel(this);

		this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		this.setLayout(null);

		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

		mapPreview.setBounds(screenWidth * 55 / 100, 30, screenWidth * 45 / 100 - 30, screenHeight * 55 / 100);
		this.add(mapPreview);

		mapListPanel.setBounds(screenWidth * 80 / 100, 50 + screenHeight * 60 / 100, screenWidth * 19 / 100 + 5, screenHeight * 20 / 100);
		System.out.println(mapListPanel.getBounds());
		this.add(mapListPanel);
		mapListPanel.setOpaque(false);
		mapListPanel.repaint();
		this.setVisible(true);
	}

	public void setMap(String mapName)
	{
		MapLoader mapLoader = new MapLoader();
		mapLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		mapInfo = mapLoader.getMapInfo();
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
}
