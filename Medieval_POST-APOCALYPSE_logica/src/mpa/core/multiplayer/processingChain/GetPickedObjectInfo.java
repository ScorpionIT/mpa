package mpa.core.multiplayer.processingChain;


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
			// GameManagerProxy.getInstance().
		}

		return super.processRequest( request );
	}
}
