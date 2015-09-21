package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class BuyPotionHandler extends ProcessingChain
{

	public BuyPotionHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] fields = request.split( ":" );

		if( fields.length == 3 && fields[0].equals( "Buy" ) )
		{
			String[] reply = new String[0];
			reply[0] = "NO";
			if( GameManagerProxy.getInstance().buyPotion( fields[1], fields[2] ) )
				reply[0] = "OK";

			return reply;

		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );

	}
}
