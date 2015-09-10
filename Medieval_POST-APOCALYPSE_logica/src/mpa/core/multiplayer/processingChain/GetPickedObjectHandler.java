package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

import com.jme3.math.Vector2f;

public class GetPickedObjectHandler extends ProcessingChain
{

	public GetPickedObjectHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{

		String[] strings = request.split( ":" );
		String[] position = strings[1].split( "," );

		if( strings.length > 1 && strings[0].equals( "what is this" ) && position.length == 2 )
		{
			String[] result = new String[1];
			result[0] = GameManagerProxy.getInstance()
					.getPickedObject(
							new Vector2f( Float.parseFloat( position[0] ), Float
									.parseFloat( position[1] ) ) );

			return result;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
