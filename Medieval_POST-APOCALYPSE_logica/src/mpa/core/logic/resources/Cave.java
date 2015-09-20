package mpa.core.logic.resources;

import mpa.core.logic.characters.Player;
import mpa.core.util.GameProperties;

public class Cave extends AbstractResourceProducer
{
	private static final int PROVIDING = 20;
	private static final int EXTRA_PROVIDING = 5;

	public Cave( float x, float y, Player player )
	{
		super( x, y, GameProperties.getInstance().getObjectWidth( "Cave" ), GameProperties
				.getInstance().getObjectHeight( "Cave" ), player ); // TODO
	}

	@Override
	public boolean providePlayer()
	{
		try
		{
			readLock.lock();
			if( !super.providePlayer() )
				return false;
			if( owner != null )
			{
				owner.putResources( "STONE", PROVIDING + getProvidingFactor() * PROVIDING
						+ EXTRA_PROVIDING * owner.getPlayerLevel().ordinal() );
				// System.out.println( "ho aggiunto pietra" );
			}
			return true;
		} finally
		{
			readLock.unlock();
		}
	}

	@Override
	public int getProviding()
	{
		return PROVIDING;
	}

}
