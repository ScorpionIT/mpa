package mpa.core.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class MapFromXMLCreator implements MapInfoCreator
{

	@Override
	public MapInfo createMapInfo(String path)
	{
		MapInfo mapInfo = null;
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try
		{
			document = builder.build(new File(path));
			mapInfo = new MapInfo();
			for (Element element : document.getRootElement().getChildren())
			{
				if (element.getName().equals("Name"))
				{
					mapInfo.setName(element.getValue());
				}
				else if (element.getName().equals("Width"))
				{
					mapInfo.setWidth(Float.parseFloat(element.getValue()));
				}
				else if (element.getName().equals("Height"))
				{
					mapInfo.setHeight(Float.parseFloat(element.getValue()));
				}
				else if (element.getName().equals("Objects"))
				{
					for (Element element2 : element.getChildren())
					{
						if (element2.getName() == "HeadQuartes")
						{
							for (mpa.core.logic.Point position : getBuildingCoordinates(element2))
							{
								mapInfo.addHeadQuarter(position);
							}
						}
						else if (element2.getName() == "Caves")
						{
							for (mpa.core.logic.Point position : getBuildingCoordinates(element2))
							{
								mapInfo.addCave(position);
							}
						}
						else if (element2.getName() == "Fields")
						{
							for (mpa.core.logic.Point position : getBuildingCoordinates(element2))
							{
								mapInfo.addField(position);
							}
						}
						else if (element2.getName() == "Mills")
						{
							for (mpa.core.logic.Point position : getBuildingCoordinates(element2))
							{
								mapInfo.addMill(position);
							}
						}
					}

				}
			}
		} catch (JDOMException | IOException e)
		{
			e.printStackTrace(); // TODO
		}

		return mapInfo;

	}

	private ArrayList<mpa.core.logic.Point> getBuildingCoordinates(Element element)
	{
		List<Element> children = element.getChildren();
		ArrayList<mpa.core.logic.Point> points = new ArrayList<>();
		for (Element element2 : children)
		{
			String value = element2.getValue();
			String[] coordinates = value.split(",");
			if (coordinates.length == 2)
			{
				points.add(new mpa.core.logic.Point(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])));
			}

		}
		return points;

	}
}
