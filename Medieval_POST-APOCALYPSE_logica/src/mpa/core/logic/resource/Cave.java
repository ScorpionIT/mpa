package mpa.core.logic.resource;

import mpa.core.logic.character.Player;
import mpa.core.util.GameProperties;

public class Cave extends AbstractResourceProducer
{
	private static final int PROVIDING = 20;
	private static final int EXTRA_PROVIDING = 5;

	public Cave(float x, float y, Player player)
	{
		super(x, y, GameProperties.getInstance().getObjectWdth("Cave"), GameProperties.getInstance().getObjectHeight("Cave"), player); // TODO
	}

	@Override
	public void providePlayer()
	{
		readLock.lock();
		if (owner != null)
			owner.putResources(Resources.STONE, PROVIDING + EXTRA_PROVIDING * owner.getPlayerLevel().ordinal());
		readLock.unlock();
	}

	@Override
	public int getProviding()
	{
		return PROVIDING;
	}

}
