package mpa.core.logic.character;

import java.util.ArrayList;
import java.util.Collection;

import mpa.core.logic.Level;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.tool.AbstractTool;

@SuppressWarnings("unused")
public class Player extends AbstractCharacter
{
	private Headquarter headquarter;
	private ArrayList<AbstractCharacter> subalterns;

	public Player(String name, int x, int y, int health, Level level,
				  Headquarter headquarter, int bagDimension)
	{
		super(name, x, y, health, bagDimension);
		this.headquarter = headquarter;
		subalterns = new ArrayList<AbstractCharacter>();
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

}
