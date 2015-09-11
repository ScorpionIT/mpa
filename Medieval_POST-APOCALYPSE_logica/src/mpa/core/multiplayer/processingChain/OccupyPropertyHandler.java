package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class OccupyPropertyHandler extends ProcessingChain
{

	public OccupyPropertyHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] strings = request.split( ":" );

		if( strings.length == 3 && strings[0].equals( "do" ) )
		{
			String[] reply = new String[1];
			if( GameManagerProxy.getInstance().occupyProperty( strings[1], strings[2] ) )
				reply[0] = new String( "ok" );
			else
				reply[0] = new String( "no" );

			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
