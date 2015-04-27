package mpa.core.logic.building;

import mpa.core.logic.character.Player;
import mpa.core.util.GameProperties;

public class House extends AbstractPrivateProperty
{

	public House(float x, float y, Player player)
	{
		super(x, y, GameProperties.getInstance().getObjectWdth("Headquarter"), GameProperties.getInstance().getObjectHeight("Headquarter"), player); // TODO
	}

}
