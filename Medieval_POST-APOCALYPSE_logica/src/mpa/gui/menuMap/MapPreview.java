package mpa.gui.menuMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.MapInfo;
import mpa.core.logic.MapLoader;
import mpa.core.logic.Pair;

public class MapPreview extends MpaPanel
{

	MapInfo mapInfo;
	HashMap<String, Image> images = new HashMap<>();
	private MainMenuPanel mainMenuPanel;

	public MapPreview(MapInfo mapInfo, MainMenuPanel mainMenuPanel)
	{
		this.mapInfo = mapInfo;
		this.mainMenuPanel = mainMenuPanel;
		this.setLayout(null);

		loadImages();
		// this.setSize(new Dimension(500, 500));
		this.setBackground(Color.GREEN);

		this.setVisible(true);

	}

	private void loadImages()
	{

		try
		{
			File folder = new File("./imagesPreview");
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png"))
				{
					System.out.println("sono " + listOfFiles[i].getName());
					Image img = ImageIO.read(new File("./imagesPreview/" + listOfFiles[i].getName()));
					img = img.getScaledInstance(W(img.getWidth(null)), H(img.getHeight(null)), 0);
					images.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4), img);
				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for (Pair<Float, Float> woodPosition : mapInfo.getWoods())
		{

			g.drawImage(images.get("wood"), X(woodPosition.getFirst()), Y(woodPosition.getSecond()), images.get("wood").getWidth(null),
					images.get("wood").getHeight(null), this);
		}

	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

		MapLoader mapLoader = new MapLoader();

		mapLoader.loadMapInfo(new MapFromXMLCreator(), "./maps/map.xml");
		MapInfo mapInfo = mapLoader.getMapInfo();
		System.out.println(mapInfo.toString());
		frame.add(new MapPreview(mapInfo, null));
		frame.setLocation(0, 0);
		frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
