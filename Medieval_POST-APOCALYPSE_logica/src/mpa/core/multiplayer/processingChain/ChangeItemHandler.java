package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class ChangeItemHandler extends ProcessingChain
{

	public ChangeItemHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] strings = request.split( ":" );
		if( strings.length == 3 && strings[0].equals( "change item" ) )
		{
			GameManagerProxy.getInstance().changeSelectedItem( strings[1], strings[2] );
			return new String[0];
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
