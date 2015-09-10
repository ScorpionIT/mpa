package mpa.core.multiplayer.processingChain;

public class PauseHandler extends ProcessingChain
{

	protected PauseHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{

		String[] strings = request.split( ":" );

		if( strings[0].equals( "PAUSE" ) )
		{
			return new String[0];
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}

}
