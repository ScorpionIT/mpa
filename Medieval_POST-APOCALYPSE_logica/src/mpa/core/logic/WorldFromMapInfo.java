package mpa.core.logic;

import mpa.core.logic.building.Market;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Mine;
import mpa.core.logic.resource.Wood;

public class WorldFromMapInfo implements WorldCreator
{
	private int woodID = 0;
	private int caveID = 0;
	private int fieldID = 0;
	private int mineID = 0;

	@Override
	public World createWorld(MapInfo mapInfo)
	{
		World world = new World(mapInfo.getWidth(), mapInfo.getHeight());
		for (Pair<Float, Float> position : mapInfo.getWoods())
		{
			Wood wood = new Wood(position.getFirst(), position.getSecond(), null);
			world.addObject(wood);
			wood.setID(String.valueOf(woodID++));

		}
		for (Pair<Float, Float> position : mapInfo.getCaves())
		{
			Cave cave = new Cave(position.getFirst(), position.getSecond(), null);
			world.addObject(cave);
			cave.setID(String.valueOf(caveID++));
		}
		for (Pair<Float, Float> position : mapInfo.getFields())
		{
			Field field = new Field(position.getFirst(), position.getSecond(), null);
			world.addObject(field);
			field.setID(String.valueOf(fieldID++));
		}
		for (Pair<Float, Float> position : mapInfo.getMines())
		{
			Mine mine = new Mine(position.getFirst(), position.getSecond(), null);
			world.addObject(mine);
			mine.setID(String.valueOf(mineID++));
		}

		Market.initiate(mapInfo.getMarket().getFirst(), mapInfo.getMarket().getSecond());

		return world;
	}
}
