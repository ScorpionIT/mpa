package mpa.core.logic.building;

import mpa.core.logic.AbstractObject;

public class FictiveSpace extends AbstractObject
{
	private AbstractBuilding building;
	
	public FictiveSpace(int x, int y, AbstractBuilding building) throws Exception
	{
		super( x, y );
		this.building = building;
	}

	public AbstractBuilding getBuilding()
	{
		return building;
	}

}
