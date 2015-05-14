package mpa.core.maths;

import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Vector2f;

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

		if( xGoal.floatValue() > world.getWidth() || xGoal.floatValue() < 0.0f
				|| yGoal.floatValue() > world.getHeight() || yGoal.floatValue() < 0.0f )
			return true;

		if( currentX < xGoal )
			objectsX = world.getObjectsXInTheRange( currentX, xGoal );
		else
			objectsX = world.getObjectsXInTheRange( xGoal, currentX );

		if( currentY < yGoal )
			objectsY = world.getObjectsYInTheRange( currentY, yGoal );
		else
			objectsY = world.getObjectsYInTheRange( yGoal, currentY );

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

		for( AbstractObject abstractObject : intersection )
		{
			if( ( pointToLineDistance( currentX, currentY, xGoal, yGoal, new Integer(
					( int ) abstractObject.getX() ), new Integer( ( int ) abstractObject.getY() ) ) - abstractObject
					.getCollisionRay() ) <= 0 )
			{
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

	public static float pointToLineDistance( float xMinLine, float yMinLine, float xMaxLine,
			float yMaxLine, float xPoint, float yPoint )
	{

		Vector2f line = new Vector2f( -xMinLine + xMaxLine, -yMinLine + yMaxLine );
		Vector2f pointToVector = new Vector2f( xPoint - xMinLine, yPoint - yMinLine );
		float scalarProduct = ( pointToVector.x * line.x + pointToVector.y * line.y )
				/ ( line.x * line.x + line.y * line.y );
		Vector2f projectionOnLine = new Vector2f( line.x * scalarProduct, line.y * scalarProduct );

		float distance = ( float ) Math.sqrt( Math.pow( pointToVector.x - projectionOnLine.x, 2 )
				+ Math.pow( pointToVector.y - projectionOnLine.y, 2 ) );
		return distance;

	}

	public static int greatestCommonDivisor( int p, int q )
	{
		if( q < 0 )
			q *= -1;

		if( p < 0 )
			p *= -1;

		if( q == 0 )
		{
			return p;
		}
		return greatestCommonDivisor( q, p % q );
	}

}
