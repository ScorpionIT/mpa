package mpa.core.logic.resource;

import mpa.core.logic.character.Player;

public class Wood extends AbstractResourceProducer
{
	private static final int PROVIDING = 35;
	private static final int EXTRA_PROVIDING = 10;

	public Wood( float x, float y, Player player )
	{
		super( x, y, player );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void providePlayer()
	{
		readLock.lock();
		if( owner != null )
			owner.putResources( Resources.WOOD, PROVIDING + EXTRA_PROVIDING
					* owner.getPlayerLevel().ordinal() );

		readLock.unlock();
	}

}
