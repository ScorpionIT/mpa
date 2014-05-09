package mpa.core.logic;

@SuppressWarnings("unused")
public abstract class AbstractBuilding extends AbstractObject
{

	private int width;
	private int height;
	private int damages; // 0-100

	public AbstractBuilding(int x, int y, int width, int height, int damages) throws Exception
	{
		super(x, y);
		this.width = width;
		this.height = height;
		if (damages < 0 || damages > 100)
			throw new Exception("damages must be greater than 0 or less than 100");
		this.damages = damages;
	}

}
