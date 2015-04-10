package mpa.core.logic.resource;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

public abstract class AbstractResourceProducer extends AbstractPrivateProperty
{

	public AbstractResourceProducer( float x, float y, Player player )
	{
		super( x, y, 0, 0, player );
	}

	public abstract void providePlayer();

}
