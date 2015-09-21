package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class GetPotionAmountHandler extends ProcessingChain
{

	public GetPotionAmountHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] fields = request.split( ":" );

		if( fields.length == 3 && fields[0].equals( "GetPotionAmount" ) )
		{
			String[] reply = new String[1];
			reply[0] = String.valueOf( GameManagerProxy.getInstance().getPotionAmount( fields[1],
					fields[2] ) );
			return reply;

		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}

}
