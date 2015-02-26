package mpa.core.logic.building;

import mpa.core.logic.character.Player;

@SuppressWarnings("unused")
public abstract class AbstractPrivateProperty extends AbstractProperty
{

	private Player owner;

	public AbstractPrivateProperty(float x, float y, float width, float height, Player owner)
	{
		super(x, y, width, height);
		this.owner = owner;// TODO
		// TODO TUTTI I BUILDING

	}

	public boolean setOwner(Player player)
	{
		this.owner = player;
		return true;
	}

	public Player getOwner()
	{
		return owner;
	}
}
