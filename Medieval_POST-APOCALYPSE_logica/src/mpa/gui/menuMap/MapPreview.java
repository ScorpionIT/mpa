package mpa.gui.menuMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;

public class MapPreview extends MpaPanel
{

	private MapInfo mapInfo;
	private HashMap<String, Image> images = new HashMap<>();
	private HashMap<JLabel, Pair<Float, Float>> headQuartersLabel = new HashMap<>();
	private MainMenuPanel mainMenuPanel;

	public MapPreview(MainMenuPanel mainMenuPanel)
	{
		this.mainMenuPanel = mainMenuPanel;
		this.setLayout(null);
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

	public void loadMap(MapInfo mapInfo)
	{
		this.mapInfo = mapInfo;
		ImageIcon imageIcon = new ImageIcon(images.get("headQuarter"));
		this.headQuartersLabel.clear();

		for (Pair<Float, Float> headQuarterPosition : mapInfo.getHeadQuarters())
		{
			JLabel jLabel = new JLabel(imageIcon);
			jLabel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					Set<JLabel> keys = MapPreview.this.headQuartersLabel.keySet();
					for (JLabel l : keys)
						if (MapPreview.this.headQuartersLabel.get(l).equals(MapPreview.this.mainMenuPanel.getSelectedHQ()))
						{
							l.setBorder(BorderFactory.createEmptyBorder());
						}
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.red));
					MapPreview.this.mainMenuPanel.setSelectedHeadQuarter(MapPreview.this.headQuartersLabel.get(e.getSource()));

				}

			});
			jLabel.setBounds(graphicX(headQuarterPosition.getFirst()), graphicY(headQuarterPosition.getSecond()), images.get("headQuarter").getWidth(null), images
					.get("headQuarter").getHeight(null));
			this.add(jLabel);

			headQuartersLabel.put(jLabel, headQuarterPosition);
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		if (mapInfo == null)
		{
			return;
		}

		for (Pair<Float, Float> woodPosition : mapInfo.getWoods())
		{

			System.out.println(woodPosition.getFirst() + " " + woodPosition.getSecond());
			g.drawImage(images.get("wood"), graphicX(woodPosition.getFirst()), graphicY(woodPosition.getSecond()), images.get("wood").getWidth(null),
					images.get("wood").getHeight(null), this);
		}
		for (Pair<Float, Float> fieldPosition : mapInfo.getFields())
		{

			// System.out.println(fieldPosition.getFirst() + " " + fieldPosition.getSecond());

			g.drawRect(graphicX(fieldPosition.getFirst()), graphicY(fieldPosition.getSecond()), images.get("headQuarter").getWidth(null), images.get("headQuarter")
					.getHeight(null));
		}

		for (Pair<Float, Float> cavesPosition : mapInfo.getCaves())
		{

			// System.out.println(cavesPosition.getFirst() + " " + cavesPosition.getSecond());

			g.drawOval(graphicX(cavesPosition.getFirst()), graphicY(cavesPosition.getSecond()), images.get("headQuarter").getWidth(null), images.get("headQuarter")
					.getHeight(null));

		}
		g.drawImage(images.get("market"), graphicX(mapInfo.getMarket().getFirst()), graphicY(mapInfo.getMarket().getSecond()),
				images.get("headQuarter").getWidth(null), images.get("market").getHeight(null), this);

	}

	@Override
	public void setMapDimension(float width, float height)
	{
		// TODO Stub di metodo generato automaticamente
		super.setMapDimension(width, height);
		loadImages();
	}

}
