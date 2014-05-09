package mpa.core.logic;

@SuppressWarnings("unused")
public abstract class AbstractResource extends AbstractObject
{

	private int providing;

	public AbstractResource(int x, int y, int providing)
	{
		super(x, y);
		this.providing = providing;
	}

}
