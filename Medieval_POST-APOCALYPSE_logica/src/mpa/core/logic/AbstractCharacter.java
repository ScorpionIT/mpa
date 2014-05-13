package mpa.core.logic;

@SuppressWarnings("unused")
public abstract class AbstractCharacter extends AbstractObject
{

	protected String name;
	protected int health; // 0 - 100
	protected Inventory bag;

	public AbstractCharacter(String name, int x, int y, int health,
			int bagDimension)
	{
		super(x, y);
		this.name = name;
		this.health = health;
		this.bag = new Inventory(bagDimension);
	}

}
