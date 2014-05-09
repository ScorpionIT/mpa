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

}
