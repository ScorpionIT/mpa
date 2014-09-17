package mpa.core.logic.building;

import mpa.core.logic.character.Player;

public class Headquarter extends House
{
	
	public Headquarter(int x, int y, int width, int height, int damages, Player player, Mill mill) throws Exception
	{
		super(x, y, width, height, damages, player, mill);
	}
	
}
