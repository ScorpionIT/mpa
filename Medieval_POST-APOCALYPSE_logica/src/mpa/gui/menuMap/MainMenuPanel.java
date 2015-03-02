package mpa.gui.menuMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.MapInfo;
import mpa.core.logic.MapLoader;

public class MainMenuPanel extends JPanel
{

	MapInfo mapInfo;
	private MapListPanel mapListPanel;

	public MainMenuPanel()
	{
		mapListPanel = new MapListPanel(this);

		this.setSize(500, 500);
		this.setLayout(null);

		this.add(mapListPanel);
		this.setVisible(true);
	}

	public void setMap(String mapName)
	{
		MapLoader mapLoader = new MapLoader();
		mapLoader.loadMapInfo(new MapFromXMLCreator(), mapName);
		mapInfo = mapLoader.getMapInfo();
		this.removeAll();
		MapPreview mapPreview = new MapPreview(mapInfo, this);
		mapPreview.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(mapPreview);
		this.updateUI();
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

		frame.setLocation(600, 300);
		frame.setSize(500, 500);
		frame.setContentPane(new MainMenuPanel());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
