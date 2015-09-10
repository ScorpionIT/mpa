package mpa.core.multiplayer.processingChain;

import java.util.HashMap;

import mpa.core.logic.GameManagerProxy;

public class GetResourcesAmountHandler extends ProcessingChain
{

	public GetResourcesAmountHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{

		String[] strings = request.split( ":" );

		if( strings.length == 1 && strings[0].equals( "getResourcesAmount" ) )
		{
			HashMap<String, HashMap<String, Integer>> playersResourceAmount = GameManagerProxy
					.getInstance().getPlayersResourceAmount();

			String[] resourcesAmount = new String[playersResourceAmount.keySet().size() * 5 + 1];

			int count = 0;

			for( String player : playersResourceAmount.keySet() )
			{
				resourcesAmount[count++] = player;
				for( String type : playersResourceAmount.get( player ).keySet() )
				{
					resourcesAmount[count++] = type + "="
							+ String.valueOf( playersResourceAmount.get( player ).get( type ) );
				}
			}

			return resourcesAmount;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
