package mpa.core.logic;

@SuppressWarnings("unused")
public abstract class AbstractCharacter extends AbstractObject
{

	private String name;
	private int health; // 0 - 100
	private Inventory bag;

	public AbstractCharacter(String name, int x, int y, int health,
			int bagDimension)
	{
		super(x, y);
		this.name = name;
		this.health = health;
		this.bag = new Inventory(bagDimension);
	}

	public boolean addToolToBag(AbstractTool tool)
	{
		return this.bag.addTool(tool);
	}

}
