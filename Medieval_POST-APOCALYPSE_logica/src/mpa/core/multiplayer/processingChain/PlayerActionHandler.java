package mpa.core.multiplayer.processingChain;

import java.util.List;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManagerProxy;

public class PlayerActionHandler extends ProcessingChain
{

	protected PlayerActionHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] strings = request.split( ":" );
		if( strings.length == 3 && strings[1].equals( "do" ) && strings[2].split( "," ).length == 2 )
		{
			String[] direction = strings[2].split( "," );

			List<String> hitPlayers = GameManagerProxy.getInstance()
					.playerAction(
							strings[0],
							new Vector2f( Integer.parseInt( direction[0] ), Integer
									.parseInt( direction[1] ) ) );

			String[] reply = new String[hitPlayers.size()];
			for( int i = 0; i < hitPlayers.size(); i++ )
			{
				if( i != hitPlayers.size() - 1 )
					reply[i] = hitPlayers.get( i ) + ",";
				else
					reply[i] = hitPlayers.get( i ) + ".";
			}
			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
