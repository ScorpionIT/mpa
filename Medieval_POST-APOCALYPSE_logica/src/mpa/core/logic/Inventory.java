package mpa.core.logic;

import java.util.ArrayList;
import java.util.Collection;

public class Inventory
{

	int capacity;
	Collection<AbstractTool> inventory;

	public Inventory(int capacity)
	{
		super();
		this.capacity = capacity;
		this.inventory = new ArrayList<AbstractTool>();
	}

	boolean addTool(AbstractTool abstractTool)
	{
		if (freeCapacity() >= abstractTool.getVolume())
		{
			inventory.add(abstractTool);
			return true;
		}
		else
			return false;

	}

	public boolean removeTool (AbstractTool tool)
	{
		if (inventory.contains(tool))
		{
			inventory.remove(tool);
			return true;
		}
		else
			return false;
		
	}
	public int freeCapacity()
	{
		int currentCapacity = 0;
		for (AbstractTool tool : inventory)
		{
			currentCapacity = currentCapacity + tool.getVolume();
		}
		return capacity - currentCapacity;
	}
	public int getCapacity()
	{
		return capacity;
	}
	
	
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public Collection<AbstractTool> getInventory()
	{
		return inventory;
	}
	
	public boolean isToolPresent (AbstractTool tool)
	{
		return inventory.contains(tool);
	}
}
