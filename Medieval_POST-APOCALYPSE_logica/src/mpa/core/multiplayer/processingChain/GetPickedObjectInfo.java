package mpa.core.multiplayer.processingChain;

import mpa.core.logic.GameManagerProxy;

public class GetPickedObjectInfo extends ProcessingChain
{

	public GetPickedObjectInfo( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		String[] strings = request.split( ":" );

		if( strings.length == 2 && strings[0].equals( "InfoOn" )
				&& strings[1].split( "," ).length == 2 )
		{
			String[] objectInfo = strings[1].split( "," );
			String objectOwner = GameManagerProxy.getInstance().getObjectOwner( objectInfo[0],
					objectInfo[1] );

			int objectProductivity = GameManagerProxy.getInstance().getObjectProductivity(
					objectInfo[0], objectInfo[1] );

			String[] reply = new String[1];
			reply[0] = objectOwner + ":" + String.valueOf( objectProductivity );

			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
