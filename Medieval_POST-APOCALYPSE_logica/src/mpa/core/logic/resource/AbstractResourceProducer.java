package mpa.core.logic.resource;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

public abstract class AbstractResourceProducer extends AbstractPrivateProperty
{

	public AbstractResourceProducer(float x, float y, int width, int height, Player player)
	{
		super(x, y, width, height, player);
	}

	public abstract void providePlayer();

	public abstract int getProviding();

}
