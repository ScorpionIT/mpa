package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class CreateTower extends ProcessingChain
{

	public CreateTower( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] fields = request.split( ":" );
		if( fields.length == 4 && fields[0].equals( "CreateTower" ) )
		{
			javax.vecmath.Vector2f towerAvaiblePosition = GameManagerProxy.getInstance()
					.getTowerAvaiblePosition( fields[2] + ":" + fields[3] );
			String[] reply = new String[1];
			reply[0] = "false:";
			if( towerAvaiblePosition != null )
			{
				String tower = GameManagerProxy.getInstance().createTower( fields[1],
						towerAvaiblePosition, fields[2] + ":" + fields[3] );

				if( !tower.equals( "" ) )
					reply[0] = "true:" + tower;

			}
			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
