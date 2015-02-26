package mpa.core.logic.character;

import mpa.core.logic.Level;
import mpa.core.logic.building.AbstractPrivateProperty;

public class DependentCharacter extends AbstractCharacter
{
	private AbstractPrivateProperty abstractPrivateProperty;
	private Level level;
	private Player boss;

	public DependentCharacter(String name, float f, float g, int health, int bagDimension, AbstractPrivateProperty abstractPrivateProperty,
			Level level, Player boss)
	{
		super(name, f, g, health, bagDimension);
		this.level = level;
		this.abstractPrivateProperty = abstractPrivateProperty;
		this.boss = boss;
	}

	public AbstractPrivateProperty getAbstractPrivateProperty()
	{
		return abstractPrivateProperty;
	}

	public void setAbstractPrivateProperty(AbstractPrivateProperty abstractPrivateProperty)
	{
		this.abstractPrivateProperty = abstractPrivateProperty;
	}

	public Level getLevel()
	{
		return level;
	}

	public boolean setLevel(Object object, Level level)
	{
		if (object == boss)
		{
			this.level = level;
			return true;
		}
		else
			return false;
	}
}
