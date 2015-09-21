package mpa.core.multiplayer.processingChain;


public class CreateTower extends ProcessingChain
{

	public CreateTower( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		if( true )
		{
			// TODO

		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
		return null;
	}
}
