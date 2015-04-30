package mpa.core.logic.resource;

import mpa.core.logic.character.Player;
import mpa.core.util.GameProperties;

public class Mine extends AbstractResourceProducer
{

	private static final int PROVIDING = 20;
	private static final int EXTRA_PROVIDING = 4;

	public Mine( float x, float y, Player player )
	{
		super( x, y, GameProperties.getInstance().getObjectWidth( "Mine" ), GameProperties
				.getInstance().getObjectHeight( "Mine" ), player );
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

	@Override
	public int getProviding()
	{
		return PROVIDING;
	}

}
