package mpa.core.logic.resource;

import mpa.core.logic.character.Player;

public class Mine extends AbstractResourceProducer
{

	private static final int PROVIDING = 20;
	private static final int EXTRA_PROVIDING = 4;

	public Mine( float x, float y, Player player )
	{
		super( x, y, player );
	}

	@Override
	public void providePlayer()
	{
		readLock.lock();

		if( owner != null )
			owner.putResources( Resources.IRON, PROVIDING + EXTRA_PROVIDING
					* owner.getPlayerLevel().ordinal() );

		readLock.unlock();
	}

}
