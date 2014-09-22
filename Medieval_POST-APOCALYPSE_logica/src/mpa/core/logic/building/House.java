package mpa.core.logic.building;


public class House extends AbstractBuilding
{
	private Mill mill;


	public House(int x, int y) throws Exception
	{
		super(x, y, 10); //TODO
		//this.mill = mill;
	}
	

	public Mill getMill()
	{
		return mill;
	}
	
	
	
	

}
