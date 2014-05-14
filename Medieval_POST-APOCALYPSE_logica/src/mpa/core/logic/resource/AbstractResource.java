package mpa.core.logic.resource;

import mpa.core.logic.AbstractObject;

@SuppressWarnings("unused")
public abstract class AbstractResource extends AbstractObject
{

	private int providing;

	public AbstractResource(int x, int y, int providing)
	{
		super(x, y);
		this.providing = providing;
	}

	public int getProviding()
	{
		return providing;
	}

	public void setProviding(int providing)
	{
		this.providing = providing;
	}
	
	

}

