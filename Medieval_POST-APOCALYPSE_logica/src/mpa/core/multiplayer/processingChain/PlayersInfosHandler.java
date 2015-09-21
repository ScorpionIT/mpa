package mpa.core.multiplayer.processingChain;

import java.util.Set;

import mpa.core.logic.GameManagerProxy;

public class PlayersInfosHandler extends ProcessingChain
{

	public PlayersInfosHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		if( request.equals( "NumberOfPlayer" ) )
		{
			String[] reply = new String[1];
			reply[0] = String.valueOf( GameManagerProxy.getInstance().getNumberOfPlayer() );
			return reply;
		}
		else if( request.equals( "PlayersNames" ) )
		{
			Set<String> playersName = GameManagerProxy.getInstance().getPlayersName();
			String[] reply = new String[playersName.size() + 1];
			int index = 0;
			for( String player : playersName )
				reply[index++] = player;

			reply[index] = ".";

			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}

}
