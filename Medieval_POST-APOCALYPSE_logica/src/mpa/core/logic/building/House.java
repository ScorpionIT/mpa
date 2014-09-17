package mpa.core.logic.building;


public class House extends AbstractBuilding
{
	private Mill mill;


	public House(int x, int y, int width, int height, int damages, Mill mill) throws Exception
	{
		super(x, y, width, height, damages);
		this.mill = mill;
	}
	

	public Mill getMill()
	{
		return mill;
	}
	
	
	
	

}
