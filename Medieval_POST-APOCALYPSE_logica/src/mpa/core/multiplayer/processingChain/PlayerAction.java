package mpa.core.multiplayer.processingChain;

public class PlayerAction extends ProcessingChain
{

	protected PlayerAction( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		return super.processRequest( request );
	}
}
