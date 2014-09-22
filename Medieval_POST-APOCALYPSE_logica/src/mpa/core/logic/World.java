package mpa.core.logic;

import java.awt.Point;
import java.util.Map;

import mpa.core.logic.building.Headquarter;

public class World
{
	private int width;
	private int height;
	private AbstractObject map[][];
	private Map<Integer, Point> headquartedPosition;
	
	public World(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
		map = new AbstractObject[this.width][this.height];
	}
	
	public boolean addObject(AbstractObject obj)
	{
		if (map[obj.getX()][obj.getY()] != null) //TODO
			return false;

		if (obj.getX() >= this.width || obj.getY() >= this.height)
			return false;
		
		map[obj.getX()][obj.getY()] = obj;
		return true;
	}
	
	public Headquarter addHeadquarter(int i) throws Exception
	{
		Headquarter headquarter;
		headquarter = new Headquarter(headquartedPosition.get(i).x, headquartedPosition.get(i).y);
		this.addObject(headquarter);
		return headquarter;
	}
	
	public void setHeadquartedPosition(Map<Integer, Point> headquartedPosition)
	{
		this.headquartedPosition = headquartedPosition;
	}
}
