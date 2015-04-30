package mpa.core.logic.building;

import mpa.core.util.GameProperties;

public class Market extends AbstractProperty
{
	private static Market market = null;

	private Market( float x, float y )
	{
		super( x, y, GameProperties.getInstance().getObjectWdth( "Market" ), GameProperties
				.getInstance().getObjectHeight( "Market" ) );

	}

	public static void initiate( float x, float y )
	{
		market = new Market( x, y );
	}

	public static Market getInstance()
	{
		return market;
	}

}
