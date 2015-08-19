package mpa.core.logic.resource;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

public abstract class AbstractResourceProducer extends AbstractPrivateProperty
{
	protected boolean working = false;

	public AbstractResourceProducer( float x, float y, int width, int height, Player player )
	{
		super( x, y, width, height, player );
	}

	public boolean providePlayer()
	{
		return working;

	}

	public abstract int getProviding();

	public void setWorking( boolean working )
	{
		this.working = working;
	}

}
