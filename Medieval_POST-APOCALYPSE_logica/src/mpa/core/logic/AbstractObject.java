package mpa.core.logic;

public abstract class AbstractObject implements InterfaceAbstractObject
{

	private float x;
	private float y;

	private float width;
	private float height;

	public AbstractObject(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	@Override
	public float getX()
	{
		return this.x;
	}

	@Override
	public float getY()
	{
		return this.y;
	}

	@Override
	public void setX(int X)
	{
		this.x = X;

	}

	@Override
	public void setY(int Y)
	{
		this.y = Y;

	}

	@Override
	public float getWidth()
	{
		return width;
	}

	@Override
	public float getHeight()
	{
		return height;
	}

	@Override
	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public void setWidth(int width)
	{
		this.width = width;
	}

	public double getCollisionRay()
	{
		float xMin = x - width / 2;
		float yMin = y - height / 2;
		return Math.sqrt(Math.pow((xMin - x), 2) + Math.pow((yMin - y), 2));
	}
}
