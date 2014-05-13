package mpa.core.logic;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class Inventory
{

	private int capacity;
	private Collection<AbstractTool> inventory;

	public Inventory(int capacity)
	{
		super();
		this.capacity = capacity;
		this.inventory = new ArrayList<AbstractTool>();
	}
	boolean addTool (AbstractTool abstractTool)
	{
		if (freeCapacity() >= abstractTool.getVolume())
		{
			inventory.add(abstractTool);
			return true;
		}
		else
			return false;
			
	}
	private int freeCapacity ()
	{
		int currentCapacity = 0;
		for (AbstractTool tool: inventory)
		{
			currentCapacity = currentCapacity + tool.getVolume();
		}
		return capacity - currentCapacity;
	}
}
