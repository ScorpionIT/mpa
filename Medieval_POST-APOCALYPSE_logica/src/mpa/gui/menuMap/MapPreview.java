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
import mpa.core.util.GameProperties;

public class MapPreview extends MpaPanel
{

	private MapInfo mapInfo;
	private HashMap<String, Image> images = new HashMap<>();
	private HashMap<JLabel, Pair<Float, Float>> headQuartersLabel = new HashMap<>();
	private MainMenuGamePanel mainMenuGamePanel;
	private Image backgroundImage;
	private String texturePath = GameProperties.getInstance().getPath("TexturePath");
	private String imagesPreviewPath = GameProperties.getInstance().getPath("ImagesPreviewPath");

	public MapPreview(MainMenuGamePanel mainMenuGamePanel)
	{
		this.mainMenuGamePanel = mainMenuGamePanel;
		this.setLayout(null);
		try
		{
			backgroundImage = ImageIO.read(new File(texturePath + "/grass-pattern.png"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		this.setVisible(true);

	}

	private void loadImages()
	{

		try
		{
			File folder = new File(imagesPreviewPath);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png"))
				{
					Image img = ImageIO.read(new File(imagesPreviewPath + "/" + listOfFiles[i].getName()));
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

		this.headQuartersLabel.clear();

		for (Pair<Float, Float> headQuarterPosition : mapInfo.getHeadQuarters())
		{
			Image image = images.get("headQuarter");
			image = image.getScaledInstance(W(GameProperties.getInstance().getObjectWidth("headQuarter")), H(GameProperties.getInstance()
					.getObjectHeight("headQuarter")), 0);

			ImageIcon imageIcon = new ImageIcon(image);
			JLabel jLabel = new JLabel(imageIcon);
			jLabel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					Set<JLabel> keys = MapPreview.this.headQuartersLabel.keySet();
					for (JLabel l : keys)
						if (MapPreview.this.headQuartersLabel.get(l).equals(MapPreview.this.mainMenuGamePanel.getSelectedHQ()))
						{
							l.setBorder(BorderFactory.createEmptyBorder());
						}
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.red));
					MapPreview.this.mainMenuGamePanel.setSelectedHeadQuarter(MapPreview.this.headQuartersLabel.get(e.getSource()));

				}

			});
			jLabel.setBounds(graphicX(headQuarterPosition.getFirst()), graphicY(headQuarterPosition.getSecond()), W(GameProperties.getInstance()
					.getObjectWidth("headQuarter")), H(GameProperties.getInstance().getObjectHeight("headQuarter")));
			this.add(jLabel);

			headQuartersLabel.put(jLabel, headQuarterPosition);
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		setBackgroundPatternImage(g);

		if (mapInfo == null)
		{
			return;
		}

		for (Pair<Float, Float> woodPosition : mapInfo.getWoods())
		{

			System.out.println(woodPosition.getFirst() + " " + woodPosition.getSecond());
			g.drawImage(images.get("wood"), graphicX(woodPosition.getFirst()), graphicY(woodPosition.getSecond()), W(GameProperties.getInstance()
					.getObjectWidth("wood")), H(GameProperties.getInstance().getObjectHeight("wood")), this);
			// g.drawImage(images.get("wood"), graphicX(woodPosition.getFirst()),
			// graphicY(woodPosition.getSecond()), images.get("wood").getWidth(null),
			// images.get("wood").getHeight(null), this);
		}
		for (Pair<Float, Float> fieldPosition : mapInfo.getFields())
		{

			g.drawImage(images.get("field"), graphicX(fieldPosition.getFirst()), graphicY(fieldPosition.getSecond()), W(GameProperties.getInstance()
					.getObjectWidth("field")), H(GameProperties.getInstance().getObjectHeight("field")), this);
			// g.drawImage(images.get("field"), graphicX(fieldPosition.getFirst()),
			// graphicY(fieldPosition.getSecond()),
			// images.get("field").getWidth(null), images.get("field").getHeight(null), this);
		}

		for (Pair<Float, Float> cavesPosition : mapInfo.getCaves())
		{
			g.drawImage(images.get("cave"), graphicX(cavesPosition.getFirst()), graphicY(cavesPosition.getSecond()), W(GameProperties.getInstance()
					.getObjectWidth("cave")), H(GameProperties.getInstance().getObjectHeight("cave")), this);
			// g.drawImage(images.get("cave"), graphicX(cavesPosition.getFirst()),
			// graphicY(cavesPosition.getSecond()), images.get("cave")
			// .getWidth(null), images.get("cave").getHeight(null), this);

		}
		// g.drawImage(images.get("market"), graphicX(mapInfo.getMarket().getFirst()),
		// graphicY(mapInfo.getMarket().getSecond()),
		// images.get("headQuarter").getWidth(null), images.get("market").getHeight(null), this);
		g.drawImage(images.get("market"), graphicX(mapInfo.getMarket().getFirst()), graphicY(mapInfo.getMarket().getSecond()), W(GameProperties
				.getInstance().getObjectWidth("market")), H(GameProperties.getInstance().getObjectHeight("market")), this);

	}

	private void setBackgroundPatternImage(Graphics g)
	{
		int backgroundWidth = backgroundImage.getWidth(this);
		int backgroundHeight = backgroundImage.getWidth(this);
		int maxI = this.getWidth() / backgroundWidth;
		int maxJ = this.getHeight() / backgroundHeight;

		for (int i = 0; i <= maxI; i++)
		{
			for (int j = 0; j <= maxJ; j++)
			{
				g.drawImage(backgroundImage, i * backgroundWidth, j * backgroundHeight, backgroundWidth, backgroundHeight, this);
			}
		}

	}

	@Override
	public void setMapDimension(float width, float height)
	{

		super.setMapDimension(width, height);
		loadImages();
	}

}
