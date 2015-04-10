package mpa.core.maths;

import java.util.ArrayList;
import java.util.Iterator;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Pair;
import mpa.core.logic.World;

public class MyMath
{
	public static double distanceInteger( Integer x1, Integer y1, Integer x2, Integer y2 )
	{
		return Math.sqrt( Math.pow( ( x2 - x1 ), 2 ) + Math.pow( ( y2 - y1 ), 2 ) );
	}

	public static float distanceFloat( float x1, float y1, float x2, float y2 )
	{
		return ( float ) Math.sqrt( Math.pow( ( x2 - x1 ), 2 ) + Math.pow( ( y2 - y1 ), 2 ) );
	}

	public static boolean isThereAnyCollision( Integer currentX, Integer currentY, Integer xGoal,
			Integer yGoal, World world )
	{
		ArrayList<AbstractObject> objectsX;
		ArrayList<AbstractObject> objectsY;

		if( currentX < xGoal )
			objectsX = world.getObjectsXInTheRange( currentX, xGoal );
		else
			objectsX = world.getObjectsXInTheRange( xGoal, currentX );

		if( currentY < yGoal )
			objectsY = world.getObjectsYInTheRange( currentY, yGoal );
		else
			objectsY = world.getObjectsYInTheRange( yGoal, currentY );

		// System.err.println( "size X " + objectsX.size() );
		// System.err.println( "size Y " + objectsY.size() );

		ArrayList<AbstractObject> intersection = new ArrayList<>();

		for( AbstractObject objectX : objectsX )
		{
			Iterator<AbstractObject> it = objectsY.iterator();
			while( it.hasNext() )
			{
				AbstractObject objectY = it.next();
				if( objectX == objectY )
				{
					intersection.add( objectX );
					it.remove();
				}
			}
		}

		// System.out.println( "size intersersection " + intersection.size() );
		for( AbstractObject abstractObject : intersection )
		{
			// System.out.println( "distance "
			// + pointToLineDistance( currentX, currentY, xGoal, yGoal, new Integer(
			// ( int ) abstractObject.getX() ),
			// new Integer( ( int ) abstractObject.getY() ) ) );
			if( ( pointToLineDistance( currentX, currentY, xGoal, yGoal, new Integer(
					( int ) abstractObject.getX() ), new Integer( ( int ) abstractObject.getY() ) ) - abstractObject
					.getCollisionRay() ) <= 0 )
			{
				// System.err.println( "c'è l'intersezione" );
				return true;
			}
		}

		return false;

	}

	public static double pointToLineDistance( Integer currentX, Integer currentY, Integer xGoal,
			Integer yGoal, Integer xObj, Integer yObj )
	{
		Pair<Integer, Integer> vector = new Pair<Integer, Integer>( -currentX + xGoal, -currentY
				+ yGoal );
		Pair<Integer, Integer> vectorToPoint = new Pair<Integer, Integer>( -currentX + xObj,
				-currentY + yObj );

		double scalarProduct = ( double ) ( ( double ) ( vector.getFirst()
				* vectorToPoint.getFirst() + vector.getSecond() * vectorToPoint.getSecond() ) / ( double ) ( vector
				.getFirst() * vector.getFirst() + vector.getSecond() * vector.getSecond() ) );

		Pair<Double, Double> proj = new Pair<Double, Double>( vectorToPoint.getFirst()
				- ( scalarProduct * vector.getFirst() ), vectorToPoint.getSecond() - scalarProduct
				* vector.getSecond() );

		return Math.sqrt( proj.getFirst() * proj.getFirst() + proj.getSecond() * proj.getSecond() );
	}

}
