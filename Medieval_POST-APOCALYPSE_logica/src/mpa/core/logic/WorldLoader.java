package mpa.core.logic;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import mpa.core.logic.building.Headquarter;
import mpa.core.logic.characters.Player;

import org.jdom2.JDOMException;

public class WorldLoader
{
	MapInfo mapInfo;

	public void loadMapInfo(MapInfoCreator mapInfoCreator, String path) throws JDOMException, IOException
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

	public int makePlayers(String playerName, World world, Pair<Float, Float> selectedHQ)
	{
		int headquarterID = 0;
		List<Pair<Float, Float>> headQuarters = mapInfo.getHeadQuarters();
		int selectedHQIndex = 0;
		for (int i = 0; i < headQuarters.size(); i++)
		{
			Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), null);
			Vector2f gatheringPlace = headquarter.getGatheringPlace();

			Player player = new Player(playerName, gatheringPlace.x, gatheringPlace.y, 100, Level.NEWBIE, headquarter);
			headquarter.setOwner(player);
			headquarter.setID(Integer.toString(headquarterID));
			world.addObject(headquarter);
			if (selectedHQ.equals(headQuarters.get(i)))
			{
				GameManager.getInstance().addPlayer(player);
				selectedHQIndex = i;
			}
			else
			{
				player.setName("Paola Maledetta " + i);
				GameManager.getInstance().addAIPlayer(player);
			}
			headquarterID++;
		}

		return selectedHQIndex;
	}

	public void makePlayers(Map<Pair<Float, Float>, String> players, World world)
	{
		int headquarterID = 0;

		List<Pair<Float, Float>> headQuarters = mapInfo.getHeadQuarters();

		for (int i = 0; i < headQuarters.size(); i++)
		{
			Headquarter headquarter = new Headquarter(headQuarters.get(i).getFirst(), headQuarters.get(i).getSecond(), null);
			Vector2f gatheringPlace = headquarter.getGatheringPlace();

			for (Pair<Float, Float> hq : players.keySet())
			{

				if (hq.getFirst().equals(headQuarters.get(i).getFirst()) && hq.getSecond().equals(headQuarters.get(i).getSecond()))
				{
					if (players.get(hq) != null)
					{
						Player player = new Player(players.get(hq), gatheringPlace.x, gatheringPlace.y, 100, Level.NEWBIE, headquarter);
						headquarter.setOwner(player);
						world.addObject(headquarter);
						headquarter.setID(Integer.toString(headquarterID));
						GameManager.getInstance().addPlayer(player);

					}
					else
					{
						Player player = new Player("Paola Maledetta " + i, gatheringPlace.x, gatheringPlace.y, 100, Level.NEWBIE, headquarter);
						headquarter.setOwner(player);
						world.addObject(headquarter);
						headquarter.setID(Integer.toString(headquarterID));
						GameManager instance = GameManager.getInstance();
						instance.addAIPlayer(player);
					}
				}
			}
			world.addObject(headquarter);
			headquarterID++;
		}

	}
}
