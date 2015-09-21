package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class ComputePathHandler extends ProcessingChain
{

	protected ComputePathHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] strings = request.split( ":" );

		if( strings.length == 3 && strings[0].equals( "GoThere" )
				&& strings[2].split( "," ).length == 2 )
		{
			String[] direction = strings[2].split( "," );
			GameManagerProxy.getInstance().computePath( strings[1],
					Float.parseFloat( direction[0] ), Float.parseFloat( direction[1] ) );

			return new String[0];
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
