package mpa.core.logic;

import mpa.core.logic.building.Market;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Wood;

public class WorldFromMapInfo implements WorldCreator
{

	@Override
	public World createWorld( MapInfo mapInfo )
	{
		World world = new World( mapInfo.getWidth(), mapInfo.getHeight() );
		for( Pair<Float, Float> position : mapInfo.getWoods() )
		{
			world.addObject( new Wood( position.getFirst(), position.getSecond(), null ) );
		}
		for( Pair<Float, Float> position : mapInfo.getCaves() )
		{
			world.addObject( new Cave( position.getFirst(), position.getSecond(), null ) );
		}
		for( Pair<Float, Float> position : mapInfo.getFields() )
		{
			world.addObject( new Field( position.getFirst(), position.getSecond(), null ) );
		}

		Market.initiate( mapInfo.getMarket().getFirst(), mapInfo.getMarket().getSecond() );

		world.addObject( Market.getInstance() );
		return world;
	}
}
