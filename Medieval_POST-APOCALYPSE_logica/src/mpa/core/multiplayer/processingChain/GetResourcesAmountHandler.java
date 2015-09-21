package mpa.core.multiplayer.processingChain;

import java.util.Map;

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

		if( strings.length == 1 && strings[0].equals( "getPlayerResourcesAmount" ) )
		{
			Map<String, Map<String, Integer>> playersResourceAmount = GameManagerProxy
					.getInstance().getPlayersResourceAmount();

			String[] resourcesAmount = new String[playersResourceAmount.keySet().size() * 5];

			int count = 0;

			for( String player : playersResourceAmount.keySet() )
			{
				resourcesAmount[count++] = "Player=" + player;
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
