package mpa.core.logic;

@SuppressWarnings("unused")
public abstract class AbstractTool extends AbstractObject
{

	private int volume;
	private int damage; // 0-10
	private int productivity; //0-10

	public AbstractTool(int x, int y, int volume, int damage, int productivity)
	{
		super(x, y);
		this.volume = volume;
		this.damage = damage;
		this.productivity = productivity;
	}

}
