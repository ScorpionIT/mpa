package mpa.core.logic;

public class MapLoader
{
	MapInfo mapInfo;

	public void loadMapInfo(MapInfoCreator mapInfoCreator, String path)
	{
		mapInfo = mapInfoCreator.createMapInfo(path);
	}

	public MapInfo getMapInfo()
	{
		return mapInfo;
	}

	public World loadWorld(WorldCreator worldCreator)
	{
		World world = worldCreator.createWorld(mapInfo);
		return world;

	}

}
