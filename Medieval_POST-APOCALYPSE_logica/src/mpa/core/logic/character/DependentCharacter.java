package mpa.core.logic.character;

import mpa.core.logic.building.House;


public class DependentCharacter extends AbstractCharacter
{
	private House house;
	
	public DependentCharacter(String name, int x, int y, int health, int bagDimension, House house)
	{
		super(name, x, y, health, bagDimension);
		this.house = house;
	}

	public House getHouse()
	{
		return house;
	}
	
	

}
