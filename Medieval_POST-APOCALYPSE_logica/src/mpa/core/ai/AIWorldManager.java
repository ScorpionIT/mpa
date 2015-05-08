package mpa.core.ai;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.maths.MyMath;

public class AIWorldManager
{

	private ArrayList<Vector2f> openList = new ArrayList<>();
	private ArrayList<Vector2f> closedList = new ArrayList<>();
	float ray;
	private int fragmentsX;
	private int fragmentsY;

	public AIWorldManager( DifficultyLevel level )
	{
		switch( level )
		{
			case ARE_YOU_KIDDING_ME:

				break;
			case EASY:
				ray = 30f;
				break;
			case EXTREME:
				ray = 200f;
				break;
			case HARD:
				ray = 100f;
				break;
			case MEDIUM:
				ray = 50f;
				break;
			default:
				break;
		}

		fragmentsX = ( int ) ( GameManager.getInstance().getWorld().getWidth() / ray );
		fragmentsY = ( int ) ( GameManager.getInstance().getWorld().getHeight() / ray );
		computeOpenList();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println( "ci sono " + openList.size() + " da visitare" );
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

	}

	private void computeOpenList()
	{
		for( int i = 1; i < fragmentsX; i++ )
			for( int j = 1; j < fragmentsY; j++ )
			{
				openList.add( new Vector2f( ray * i, ray * j ) );
			}
	}

	Vector2f getNextLocation( Player player )
	{
		Vector2f playerPosition = new Vector2f( player.getX(), player.getY() );
		float minDistance = Float.MAX_VALUE;
		Vector2f nextLocation = null;

		for( Vector2f node : openList )
		{
			float newDistance = MyMath.distanceFloat( playerPosition.x, playerPosition.y, node.x,
					node.y );
			if( !closedList.contains( node ) && newDistance < minDistance )
			{
				minDistance = newDistance;
				nextLocation = node;
			}

		}

		if( nextLocation != null )
		{
			openList.remove( nextLocation );
			closedList.add( nextLocation );
		}

		return nextLocation;
	}
}
