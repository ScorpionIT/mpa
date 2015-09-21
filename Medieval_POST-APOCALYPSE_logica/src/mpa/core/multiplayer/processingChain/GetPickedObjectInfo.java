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

		if( strings.length == 3 && strings[0].equals( "InfoOn" )
				&& strings[2].split( "," ).length == 2 )
		{
			String[] objectInfo = strings[2].split( "," );
			String info = new String();
			if( strings[1].equals( "OWNER" ) )
				info = GameManagerProxy.getInstance().getObjectOwner( objectInfo[0], objectInfo[1] );
			else if( strings[1].equals( "PRODUCTIVITY" ) )
				info = String.valueOf( GameManagerProxy.getInstance().getObjectProductivity(
						objectInfo[0], objectInfo[1] ) );

			int objectProductivity = GameManagerProxy.getInstance().getObjectProductivity(
					objectInfo[0], objectInfo[1] );

			String[] reply = new String[1];
			reply[0] = info + ":" + String.valueOf( objectProductivity );

			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
