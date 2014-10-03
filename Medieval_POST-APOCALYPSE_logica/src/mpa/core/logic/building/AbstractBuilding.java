package mpa.core.logic.building;

import mpa.core.logic.AbstractObject;

@SuppressWarnings("unused")
public abstract class AbstractBuilding extends AbstractObject
{

	private int width;
	private int height;
	
	public AbstractBuilding( int x, int y ) 
	{
		super(x, y);
		
	}

}
