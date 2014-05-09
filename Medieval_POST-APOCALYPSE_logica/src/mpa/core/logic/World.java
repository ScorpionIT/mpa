package mpa.core.logic;

public class World
{

	private int width;
	private int height;
	private AbstractObject map[][];

	public World(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
		map = new AbstractObject[this.width][this.height];
	}

	public boolean addObject(AbstractObject obj)
	{
		if (map[obj.getX()][obj.getY()] == null)
			return false;

		map[obj.getX()][obj.getY()] = obj;
		return true;
	}
}
