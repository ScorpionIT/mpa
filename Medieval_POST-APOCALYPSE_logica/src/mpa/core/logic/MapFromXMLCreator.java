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

		// TODO controllare che la struttura del file sia corretta prima di caricare.

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
				else if (element.getName().equals("Number"))
				{
					mapInfo.setNumberOfPlayers(Integer.parseInt(element.getValue()));
				}

				else if (element.getName().equals("Objects"))
				{
					for (Element element2 : element.getChildren())
					{
						if (element2.getName() == "HeadQuarters")
						{
							for (Pair<Float, Float> position : getBuildingCoordinates(element2))
							{
								mapInfo.addHeadQuarter(position);
							}
						}
						else if (element2.getName() == "Caves")
						{
							for (Pair<Float, Float> position : getBuildingCoordinates(element2))
							{
								mapInfo.addCave(position);
							}
						}
						else if (element2.getName() == "Fields")
						{
							for (Pair<Float, Float> position : getBuildingCoordinates(element2))
							{
								mapInfo.addField(position);
							}
						}
						else if (element2.getName() == "Mills")
						{
							for (Pair<Float, Float> position : getBuildingCoordinates(element2))
							{
								mapInfo.addMill(position);
							}
						}

						else if (element2.getName() == "Woods")
						{
							for (Pair<Float, Float> position : getBuildingCoordinates(element2))
							{
								mapInfo.addWood(position);
							}
						}

						else if (element2.getName() == "Market")
						{
							String value = element2.getValue();
							String[] coordinates = value.split(",");
							if (coordinates.length == 2)
							{
								mapInfo.setMarket(new Pair<Float, Float>(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])));
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

	private ArrayList<Pair<Float, Float>> getBuildingCoordinates(Element element)
	{
		List<Element> children = element.getChildren();
		ArrayList<Pair<Float, Float>> points = new ArrayList<>();
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
