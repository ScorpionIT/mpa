package mpa.core.logic.building;

import mpa.core.logic.character.Player;

public class House extends AbstractBuilding
{
	private Player player;
	private Mill mill;


	public House(int x, int y, int width, int height, int damages, Player player, Mill mill) throws Exception
	{
		super(x, y, width, height, damages);
		this.player = player;
		this.mill = mill;
	}
	
	public Player getOwner()
	{
		return this.player;
	}

	public Mill getMill()
	{
		return mill;
	}
	
	
	
	

}
