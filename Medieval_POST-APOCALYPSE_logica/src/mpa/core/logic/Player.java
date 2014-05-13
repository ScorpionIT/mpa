package mpa.core.logic;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class Player extends AbstractCharacter
{
	private Headquarter headquarter;
	private Collection<DependentPlayer> subalterns;

	public Player(String name, int x, int y, int health, Level level,
			Headquarter headquarter, int bagDimension)
	{
		super(name, x, y, health, bagDimension);
		this.headquarter = headquarter;
		subalterns = new ArrayList<DependentPlayer>();
	}

	// public pickUp
}
