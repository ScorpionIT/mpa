package mpa.core.logic.character;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Inventory;

public abstract class AbstractCharacter extends AbstractObject
{

	protected String name;
	protected int health; // 0 - 100
	protected Inventory bag;

	public AbstractCharacter(String name, float x, float y, int health, int bagDimension)
	{
		super(x, y, 0, 0); // TODO
		this.name = name;
		this.health = health;
		this.bag = new Inventory(bagDimension);
	}

}
