package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class World
{
	private float width;
	private float height;
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectX = new HashMap<>();
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectY = new HashMap<>();

	// private Map<Integer, Point> headquartedPosition;

	public World(float width, float height)
	{
		super();
		this.width = width;
		this.height = height;

	}

	public boolean addObject(AbstractObject obj)
	{
		float x = obj.getX();
		float y = obj.getY();
		float width = obj.getWidth();
		float height = obj.getHeight();
		float xMin = x - width / 2;
		float xMax = x + width / 2;
		float yMin = y - height / 2;
		float yMax = y + height / 2;

		Pair<Float, Float> xPair = new Pair(xMin, xMax);
		Pair<Float, Float> yPair = new Pair(yMin, yMax);
		if (!objectX.containsKey(xPair))
			objectX.put(xPair, new ArrayList<AbstractObject>());

		objectX.get(xPair).add(obj);

		if (!objectY.containsKey(yPair))
			objectY.put(yPair, new ArrayList<AbstractObject>());

		objectY.get(yPair).add(obj);

		// TODO vedere se ci sono oggetti sovrapposti
		return true;
	}

	// per aggiungere giocatori a partita iniziata
	// public Headquarter addHeadquarter(int i) throws Exception
	// {
	// Headquarter headquarter;
	// headquarter = new Headquarter(headquartedPosition.get(i).x, headquartedPosition.get(i).y);
	// this.addObject(headquarter);
	// return headquarter;
	// }

	// public void setHeadquartedPosition(Map<Integer, Point> headquartedPosition)
	// {
	// this.headquartedPosition = headquartedPosition;
	// }

	public HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> getObjectX()
	{
		return objectX;
	}

	public HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> getObjectY()
	{
		return objectY;
	}

	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}

	// public boolean isPositionEmpty(int x, int y)
	// {
	// return (map[x][y] instanceof EmptyObject);
	// }

}
