package mpa.gui.prova;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.MapInfo;
import mpa.core.logic.Pair;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Market;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Wood;
import mpa.gui.menuMap.MpaPanel;

public class GameGui extends MpaPanel
{

	private MapInfo mapInfo;
	private HashMap<String, Image> images = new HashMap<>();
	private HashMap<JLabel, Pair<Float, Float>> headQuartersLabel = new HashMap<>();

	public GameGui()
	{
		this.setLayout(null);

		this.setBackground(Color.GREEN);
		this.setMapDimension(GameManager.getInstance().getWorld().getWidth(), GameManager.getInstance().getWorld().getHeight());

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

	@Override
	protected void paintComponent(Graphics g)
	{

		super.paintComponent(g);

		// System.out.println(images.get("headQuarter").getWidth(null));
		// System.out.println(images.get("headQuarter").getHeight(null));

		HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectX = GameManager.getInstance().getWorld().getObjectX();
		Set<Pair<Float, Float>> keySet = objectX.keySet();
		for (Pair<Float, Float> pair : keySet)
		{
			for (AbstractObject obj : objectX.get(pair))
			{
				if (obj instanceof Wood)
					g.drawImage(images.get("wood"), X(obj.getX()), Y(obj.getY()), images.get("wood").getWidth(null),
							images.get("wood").getHeight(null), this);

				else if (obj instanceof Headquarter)
					g.drawImage(images.get("headQuarter"), X(obj.getX()), Y(obj.getY()), images.get("wood").getWidth(null), images.get("wood")
							.getHeight(null), this);

				else if (obj instanceof Cave)
					g.drawOval(X(obj.getX()), Y(obj.getY()), images.get("headQuarter").getWidth(null), images.get("headQuarter").getHeight(null));
				else if (obj instanceof Market)
					g.drawImage(images.get("market"), X(obj.getX()), Y(obj.getY()), images.get("headQuarter").getWidth(null), images.get("market")
							.getHeight(null), this);
				else if (obj instanceof Field)
					g.drawRect(X(obj.getX()), Y(obj.getY()), images.get("headQuarter").getWidth(null), images.get("headQuarter").getHeight(null));

			}
		}

		List<Player> players = GameManager.getInstance().getPlayers();
		for (Player player : players)
		{
			// System.out.println(player.getX() + " - " + player.getY());
			g.drawRoundRect(X(player.getX()), Y(player.getY()), 10, 10, 10, 20);
		}

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		loadImages();
	}

}
