package mpa.core.logic.resource;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

public abstract class AbstractResourceProducer extends AbstractPrivateProperty
{

	private float providing;

	public AbstractResourceProducer(float x, float y, float providing, Player player)
	{
		super(x, y, 0, 0, player);
		this.providing = providing;
	}

	public float getProviding()
	{
		return providing;
	}

	public void setProviding(float providing)
	{
		this.providing = providing;
	}

}
