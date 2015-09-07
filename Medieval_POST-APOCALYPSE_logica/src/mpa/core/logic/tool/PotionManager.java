package mpa.core.logic.tool;

import java.util.HashMap;

import mpa.core.logic.resource.Resources;

public class PotionManager
{
	private static PotionManager _instance = null;

	private HashMap<Potions, HashMap<Resources, Integer>> priceList;

	private PotionManager()
	{
		priceList = new HashMap<>();

		HashMap<Resources, Integer> price = new HashMap<>();
		price.put( Resources.WHEAT, 10 );
		price.put( Resources.HERBS, 15 );
		price.put( Resources.STONE, 0 );
		price.put( Resources.IRON, 0 );
		price.put( Resources.WOOD, 0 );
		priceList.put( Potions.HP, price );

		price.clear();
		price.put( Resources.WHEAT, 8 );
		price.put( Resources.HERBS, 9 );
		price.put( Resources.STONE, 0 );
		price.put( Resources.IRON, 0 );
		price.put( Resources.WOOD, 10 );
		priceList.put( Potions.MP, price );

		price.clear();
		price.put( Resources.WHEAT, 0 );
		price.put( Resources.HERBS, 9 );
		price.put( Resources.STONE, 15 );
		price.put( Resources.IRON, 18 );
		price.put( Resources.WOOD, 14 );
		priceList.put( Potions.GRANADE, price );

		price.clear();
		price.put( Resources.WHEAT, 10 );
		price.put( Resources.HERBS, 9 );
		price.put( Resources.STONE, 15 );
		price.put( Resources.IRON, 14 );
		price.put( Resources.WOOD, 12 );
		priceList.put( Potions.FLASH_BANG, price );

	}

	public static PotionManager getInstance()
	{
		if( _instance == null )
			_instance = new PotionManager();

		return _instance;
	}

	public HashMap<Resources, Integer> getPrice( Potions type )
	{
		return this.priceList.get( type );
	}

}
