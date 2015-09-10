package mpa.core.multiplayer.processingChain;

public class PlayerActionHandler extends ProcessingChain
{

	protected PlayerActionHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		return super.processRequest( request );
	}
}
