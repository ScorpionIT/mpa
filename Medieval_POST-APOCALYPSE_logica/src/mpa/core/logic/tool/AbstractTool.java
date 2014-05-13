package mpa.core.logic.tool;

import mpa.core.logic.AbstractObject;

public abstract class AbstractTool extends AbstractObject
{

	int volume;
	int damage; // 0-10
	int productivity; // 0-10

	public AbstractTool(int x, int y, int volume, int damage, int productivity)
	{
		super(x, y);
		this.volume = volume;
		this.damage = damage;
		this.productivity = productivity;
	}

	public int getVolume()
	{
		return this.volume;
	}

	public int getDamage()
	{
		return damage;
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	public int getProductivity()
	{
		return productivity;
	}

	public void setProductivity(int productivity)
	{
		this.productivity = productivity;
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}

}
