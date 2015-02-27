package mpa.core.logic;

public class MapLoader
{
	MapInfo mapInfo;

	void loadMapInfo(MapInfoCreator mapInfoCreator, String path)
	{
		mapInfo = mapInfoCreator.createMapInfo(path);
	}

	public MapInfo getMapInfo()
	{
		return mapInfo;
	}

	public void loadWorld(WorldCreator worldCreator, String path)
	{
		World world = worldCreator.createWorld(path);

	}

}
