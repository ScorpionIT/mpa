package mpa.core.logic.building;

import mpa.core.util.GameProperties;

public class Market extends AbstractProperty
{

	public Market(float x, float y)
	{
		super(x, y, GameProperties.getInstance().getObjectWdth("Market"), GameProperties.getInstance().getObjectHeight("Market"));

	}

}
