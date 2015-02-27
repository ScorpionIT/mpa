package mpa.core.logic.resource;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.Player;

public abstract class AbstractResource extends AbstractPrivateProperty
{

	private int providing;

	public AbstractResource( int x, int y, int providing, Player player )
	{
		super( x, y, 0, 0, player );
		this.providing = providing;
	}

	public int getProviding()
	{
		return providing;
	}

	public void setProviding( int providing )
	{
		this.providing = providing;
	}

}
