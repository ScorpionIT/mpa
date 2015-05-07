package mpa.core.util;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import mpa.core.logic.Pair;

import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class MapToXmlCreator
{
	private static String path = GameProperties.getInstance().getPath("CustomMapsPath");

	private static void printOnFile(Element element, String mapName)
	{
		XMLOutputter output = new XMLOutputter();
		try
		{
			OutputStream outputStream = new FileOutputStream(new File(path + "/" + mapName + ".xml"));
			output.output(element, outputStream);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void createXmlMap(Stack<Pair<String, Point>> objectsPosition, String mapName, int mapWidth, int mapHeight, int numberOfPlayers)
	{

		Element map = new Element("Map");
		map.addContent(new Element("Name").setText(mapName));
		map.addContent(new Element("Width").setText(Integer.toString(mapWidth)));
		map.addContent(new Element("Height").setText(Integer.toString(mapHeight)));
		map.addContent(new Element("Number").setText(Integer.toString(numberOfPlayers)));

		Element headquarters = new Element("Headquarters");
		Element fields = new Element("Fields");
		Element caves = new Element("Caves");
		Element mines = new Element("Mines");
		Element woods = new Element("Woods");
		Element mills = new Element("Mills");
		Element market = null;

		for (Pair<String, Point> object : objectsPosition)
		{
			if (object.getFirst().toLowerCase().equals("headquarter"))
			{
				Element element = new Element("Headquarter");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				headquarters.addContent(element);

			}
			else if (object.getFirst().toLowerCase().equals("field"))
			{
				Element element = new Element("Field");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				fields.addContent(element);
			}
			else if (object.getFirst().toLowerCase().equals("wood"))
			{
				Element element = new Element("Wood");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				woods.addContent(element);

			}
			else if (object.getFirst().toLowerCase().equals("mill"))
			{
				Element element = new Element("Mill");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				mills.addContent(element);

			}
			else if (object.getFirst().toLowerCase().equals("market"))
			{
				market = new Element("Market");
				market.setText(object.getSecond().x + "," + object.getSecond().y);

			}
			else if (object.getFirst().toLowerCase().equals("cave"))
			{
				Element element = new Element("Cave");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				caves.addContent(element);
			}
			else if (object.getFirst().toLowerCase().equals("mine"))
			{
				Element element = new Element("Mine");
				element.setText(object.getSecond().x + "," + object.getSecond().y);
				mines.addContent(element);
			}

		}

		Element objects = new Element("Objects");

		objects.addContent(headquarters);
		if (fields.getChildren().size() != 0)
			objects.addContent(fields);
		if (mills.getChildren().size() != 0)
			objects.addContent(mills);
		if (mines.getChildren().size() != 0)
			objects.addContent(mines);
		if (caves.getChildren().size() != 0)
			objects.addContent(caves);
		if (woods.getChildren().size() != 0)
			objects.addContent(woods);
		if (market != null)
			objects.addContent(market);

		map.addContent(objects);
		printOnFile(map, mapName);

	}

	// public static void print(Element element)
	// {
	// XMLOutputter output = new XMLOutputter();
	// try
	// {
	// output.output(element, System.out);
	// } catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	//
	// }
}
