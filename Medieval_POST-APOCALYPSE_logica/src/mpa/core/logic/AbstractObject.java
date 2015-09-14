package mpa.core.logic;

import javax.vecmath.Vector2f;

public abstract class AbstractObject implements InterfaceAbstractObject
{

	protected float x;
	protected float y;

	protected float width;
	protected float height;
	private String ID;

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
	public void setX(float X)
	{
		this.x = X;

	}

	@Override
	public void setY(float Y)
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

	public void setID(String ID)

	{
		this.ID = ID;
	}

	public String getID()
	{
		return ID;
	}

	public float getCollisionRay()
	{
		float xMin = x - width / 2;
		float yMin = y - height / 2;
		float collisionRay = (float) Math.sqrt(Math.pow((xMin - x), 2) + Math.pow((yMin - y), 2));

		return collisionRay;
	}

	public Vector2f getPosition()
	{
		return new Vector2f(x, y);
	}
}
