package mpa.core.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mpa.core.logic.MapInfo;
import mpa.core.logic.MapInfoCreator;
import mpa.core.logic.Pair;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class MapFromXMLCreator implements MapInfoCreator
{

	@Override
	public MapInfo createMapInfo(String path) throws JDOMException, IOException
	{

		MapInfo mapInfo = null;
		SAXBuilder builder = new SAXBuilder();
		Document document;

		document = builder.build(new File(path));
		mapInfo = new MapInfo();
		for (Element element : document.getRootElement().getChildren())
		{
			if (element.getName().toLowerCase().equals("name"))
			{
				mapInfo.setName(element.getValue());
			}
			else if (element.getName().toLowerCase().equals("width"))
			{
				mapInfo.setWidth(Float.parseFloat(element.getValue()));
			}
			else if (element.getName().toLowerCase().equals("height"))
			{
				mapInfo.setHeight(Float.parseFloat(element.getValue()));
			}
			else if (element.getName().toLowerCase().equals("number"))
			{
				mapInfo.setNumberOfPlayers(Integer.parseInt(element.getValue()));
			}

			else if (element.getName().toLowerCase().equals("objects"))
			{
				for (Element element2 : element.getChildren())
				{
					if (element2.getName().toLowerCase().equals("headquarters"))
					{
						for (Pair<Float, Float> position : getBuildingCoordinates(element2))
						{
							mapInfo.addHeadQuarter(position);
						}
					}
					else if (element2.getName().toLowerCase().equals("caves"))
					{
						for (Pair<Float, Float> position : getBuildingCoordinates(element2))
						{
							mapInfo.addCave(position);
						}
					}
					else if (element2.getName().toLowerCase().equals("fields"))
					{
						for (Pair<Float, Float> position : getBuildingCoordinates(element2))
						{
							mapInfo.addField(position);
						}
					}
					else if (element2.getName().toLowerCase().equals("mines"))
					{
						for (Pair<Float, Float> position : getBuildingCoordinates(element2))
						{
							mapInfo.addMine(position);
						}
					}

					else if (element2.getName().toLowerCase().equals("woods"))
					{
						for (Pair<Float, Float> position : getBuildingCoordinates(element2))
						{
							mapInfo.addWood(position);
						}
					}

				}

			}
		}

		return mapInfo;

	}

	private List<Pair<Float, Float>> getBuildingCoordinates(Element element)
	{
		List<Element> children = element.getChildren();
		List<Pair<Float, Float>> points = new ArrayList<>();
		for (Element element2 : children)
		{
			String value = element2.getValue();
			String[] coordinates = value.split(",");
			if (coordinates.length == 2)
			{
				points.add(new Pair<Float, Float>(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])));
			}

		}
		return points;

	}
}
