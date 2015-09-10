package mpa.core.multiplayer.processingChain;


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
		if( strings.length == 2 && strings[0].equals( "change item" ) )
		{
			// GameManagerProxy.getInstance().changeSelectedItem( p, selected );
		}

		return super.processRequest( request );
	}
}
