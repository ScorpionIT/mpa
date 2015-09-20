package mpa.core.logic.potions;

import java.util.HashMap;

public class PotionManager
{
	private static PotionManager _instance = null;

	private HashMap<Potions, HashMap<String, Integer>> priceList;

	private PotionManager()
	{
		priceList = new HashMap<>();

		HashMap<String, Integer> price = new HashMap<>();
		price.put( "WHEAT", 10 );
		price.put( "HERBS", 15 );
		price.put( "STONE", 0 );
		price.put( "IRON", 0 );
		price.put( "WOOD", 0 );
		priceList.put( Potions.HP, price );

		price.clear();
		price.put( "WHEAT", 8 );
		price.put( "HERBS", 9 );
		price.put( "STONE", 0 );
		price.put( "IRON", 0 );
		price.put( "WOOD", 10 );
		priceList.put( Potions.MP, price );

		price.clear();
		price.put( "WHEAT", 0 );
		price.put( "HERBS", 9 );
		price.put( "STONE", 15 );
		price.put( "IRON", 18 );
		price.put( "WOOD", 14 );
		priceList.put( Potions.GRANADE, price );

		price.clear();
		price.put( "WHEAT", 10 );
		price.put( "HERBS", 9 );
		price.put( "STONE", 15 );
		price.put( "IRON", 14 );
		price.put( "WOOD", 12 );
		priceList.put( Potions.FLASH_BANG, price );

	}

	public static PotionManager getInstance()
	{
		if( _instance == null )
			_instance = new PotionManager();

		return _instance;
	}

	public HashMap<String, Integer> getPrice( Potions type )
	{
		return this.priceList.get( type );
	}

}
