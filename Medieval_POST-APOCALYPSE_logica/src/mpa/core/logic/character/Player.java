package mpa.core.logic.character;


import java.util.ArrayList;
import java.util.Collection;

import mpa.core.logic.Level;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.tool.AbstractTool;
import mpa.core.logic.resource.Field;

@SuppressWarnings("unused")
public class Player extends AbstractCharacter
{
	private Headquarter headquarter;
	private ArrayList<AbstractCharacter> subalterns;
	private Level level;
	private ArrayList<Field> fields;

	public Player(String name, int x, int y, int health, Level level,
				  Headquarter headquarter, int bagDimension)
	{
		super(name, x, y, health, bagDimension);
		this.headquarter = headquarter;
		subalterns = new ArrayList<AbstractCharacter>();
		this.level= level;
	}

	public boolean pickUpTool(AbstractTool tool)
	{
		return this.bag.addTool(tool);
	}

	public boolean throwTool(AbstractTool tool)
	{
		return this.bag.removeTool(tool);
	}
	
	public boolean addSubaltern(AbstractCharacter subaltern)
	{
		return this.subalterns.add(subaltern);
	}
	
	public Level getPlayerLevel()
	{
		return this.level;
	}

	public ArrayList<AbstractCharacter> getSubalterns()
	{
		return subalterns;
	}

}
