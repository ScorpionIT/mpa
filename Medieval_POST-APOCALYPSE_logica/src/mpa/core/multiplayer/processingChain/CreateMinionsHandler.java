package mpa.core.multiplayer.processingChain;

import java.util.List;

import mpa.core.logic.GameManagerProxy;

public class CreateMinionsHandler extends ProcessingChain
{

	public CreateMinionsHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		// TODO towerCrusher sono creati uno alla volta
		String[] strings = request.split( ":" );

		if( strings.length == 5 && strings[0].equals( "blessMinions" ) )
		{
			List<String> minions;
			if( strings[2].equals( "minions" ) )
				minions = GameManagerProxy.getInstance().createMinions( strings[1],
						Integer.parseInt( strings[3] ), strings[4] );

			// else
			// minions = GameManagerProxy.getInstance().createTowerCrushers( strings[1],
			// Integer.parseInt( strings[3] ), strings[4] );
			// String[] reply = new String[minions.size()];
			//
			// for (int i = 0; i < minions.size(); i++)
			// if (i != minions.size() - 1)
			// reply[i] = minions.get(i) + ",";
			// else
			// reply[i] = minions.get(i) + ".";

			// return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
		return null;
	}
}
