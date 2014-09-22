package mpa.core.logic.building;

import mpa.core.logic.AbstractObject;

@SuppressWarnings("unused")
public abstract class AbstractBuilding extends AbstractObject
{

	private int width;
	private int height;
	private int damages; // 0-100

	public AbstractBuilding(int x, int y, int damages) throws Exception
	{
		super(x, y);
		if (damages < 0 || damages > 100)
			throw new Exception("damages must be greater than 0 or less than 100");
		this.damages = damages;
	}

}
