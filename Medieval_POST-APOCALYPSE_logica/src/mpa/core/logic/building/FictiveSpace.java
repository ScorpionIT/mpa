package mpa.core.logic.building;

import mpa.core.logic.AbstractObject;

public class FictiveSpace extends AbstractObject
{
	private AbstractBuilding building;
	
	public FictiveSpace(int x, int y) throws Exception
	{
		super( x, y );
	}
	
	public void setBuilding(AbstractBuilding building)
	{
		this.building = building;
	}
	
	public AbstractBuilding getBuilding()
	{
		return building;
	}

}
