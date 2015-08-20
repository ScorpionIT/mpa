package mpa.core.logic.building;

import mpa.core.logic.resource.Resources;
import mpa.core.util.GameProperties;

public class Market extends AbstractProperty
{
	private static Market market = null;

	// PRICES
	private final int STONE_PRICE_BUY = 5;
	private final int STONE_PRICE_SELL = 7;
	private final int WHEAT_PRICE_BUY = 8;
	private final int WHEAT_PRICE_SELL = 12;
	private final int IRON_PRICE_BUY = 6;
	private final int IRON_PRICE_SELL = 8;
	private final int WOOD_PRICE_BUY = 4;
	private final int WOOD_PRICE_SELL = 5;
	private final int HERBS_PRICE_BUY = 12;
	private final int HERBS_PRICE_SELL = 15;

	private Market( float x, float y )
	{
		super( x, y, GameProperties.getInstance().getObjectWidth( "Market" ), GameProperties
				.getInstance().getObjectHeight( "Market" ) );

	}

	public static void initiate( float x, float y )
	{
		market = new Market( x, y );
	}

	public int priceBuy( Resources r )
	{
		switch( r )
		{
			case HERBS:
				return HERBS_PRICE_BUY;
			case IRON:
				return IRON_PRICE_BUY;
			case STONE:
				return STONE_PRICE_BUY;
			case WHEAT:
				return WHEAT_PRICE_BUY;
			case WOOD:
				return WOOD_PRICE_BUY;
		}
		return 0;
	}

	public int priceSell( Resources r )
	{
		switch( r )
		{
			case HERBS:
				return HERBS_PRICE_SELL;
			case IRON:
				return IRON_PRICE_SELL;
			case STONE:
				return STONE_PRICE_SELL;
			case WHEAT:
				return WHEAT_PRICE_SELL;
			case WOOD:
				return WOOD_PRICE_SELL;
		}
		return 0;
	}

	public static Market getInstance()
	{
		return market;
	}

}
