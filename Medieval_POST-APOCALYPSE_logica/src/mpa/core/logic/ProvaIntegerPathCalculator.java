package mpa.core.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class ProvaIntegerPathCalculator
{

	private static float increment = 30;

	public ArrayList<Pair<Integer, Integer>> computePath( World world, float xGoal, float yGoal,
			float xPlayer, float yPlayer )
	{

		ArrayList<PathNode> openList = new ArrayList<>();

		ArrayList<PathNode> closedList = new ArrayList<>();
		PathNode currentPosition = new PathNode( null, new Pair<Integer, Integer>( ( int ) xPlayer,
				( int ) yPlayer ), 0 );
		Pair<Float, Float> currentVector = new Pair<Float, Float>( ( xGoal - xPlayer ) / 300,
				( yGoal - yPlayer ) / 300 );
		Pair<Float, Float> normalVector = new Pair<Float, Float>( -currentVector.getSecond(),
				currentVector.getFirst() );

		openList.add( currentPosition );
		while( openList.size() != 0 )
		{
			currentPosition = pointWithLowestCost( openList, xGoal, yGoal, world );

			closedList.add( currentPosition );
			openList.remove( currentPosition );
			if( !isThereAnyCollision( currentPosition.getCurrentNode().getFirst(), currentPosition
					.getCurrentNode().getSecond(), new Integer( ( int ) xGoal ), new Integer(
					( int ) yGoal ), world ) )
			{
				Pair<Integer, Integer> goal = new Pair<Integer, Integer>( ( int ) xGoal,
						( int ) yGoal );
				closedList.add( new PathNode( currentPosition.getCurrentNode(), goal, computeCost(
						currentPosition, goal ) ) );
				break;
			}

			/* TODO use geometry to make the shit up here work */
			if( distance( currentPosition.getCurrentNode().getFirst(), currentPosition
					.getCurrentNode().getSecond(), ( int ) xGoal, ( int ) yGoal ) < increment )
			{
				Pair<Integer, Integer> goal = new Pair<Integer, Integer>( ( int ) xGoal,
						( int ) yGoal );
				closedList.add( new PathNode( currentPosition.getCurrentNode(), goal, computeCost(
						currentPosition, goal ) ) );
				break;
			}

			ArrayList<Pair<Float, Float>> points = new ArrayList<>();

			// nord
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					+ increment * currentVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() + increment * currentVector.getSecond() ) );

			// sud
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					- increment * currentVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() - increment * currentVector.getSecond() ) );
			//

			// est
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					- increment * normalVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() - increment * normalVector.getSecond() ) );

			// ovest
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					+ increment * normalVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() + increment * normalVector.getSecond() ) );

			for( Pair<Float, Float> pair : points )
			{
				Pair<Integer, Integer> point = new Pair<Integer, Integer>( new Integer( pair
						.getFirst().intValue() ), new Integer( pair.getSecond().intValue() ) );
				double computeCost = computeCost( currentPosition, point );

				if( !isThereAnyCollision( currentPosition.getCurrentNode().getFirst(),
						currentPosition.getCurrentNode().getSecond(), point.getFirst(),
						point.getSecond(), world ) )
				{
					if( !contains( closedList, currentPosition.getCurrentNode(), point, computeCost ) )
					{
						if( !contains( openList, currentPosition.getCurrentNode(), point,
								computeCost ) )
						{
							openList.add( new PathNode( currentPosition.getCurrentNode(), point,
									computeCost ) );
						}
					}

				}
			}

		}

		return getSmartestPath( getPath( closedList ) );

	}

	private ArrayList<Pair<Integer, Integer>> getSmartestPath(
			ArrayList<Pair<Integer, Integer>> path )
	{
		if( path.size() == 2 )
			return path;
		ArrayList<Pair<Integer, Integer>> finalPath = new ArrayList<>();

		int currentIndex = 0;
		int currentFurthestPoint = 1;
		finalPath.add( path.get( currentIndex ) );

		while( currentIndex < path.size() - 1 && currentFurthestPoint < path.size() )
		{
			Pair<Integer, Integer> currentPair = path.get( currentIndex );
			if( currentFurthestPoint == path.size() - 1 )
			{
				finalPath.add( path.get( currentFurthestPoint ) );
				break;
			}
			Pair<Integer, Integer> pointToConsider = path.get( currentFurthestPoint + 1 );
			if( !isThereAnyCollision( currentPair.getFirst(), currentPair.getSecond(),
					pointToConsider.getFirst(), pointToConsider.getSecond(), GameManager
							.getInstance().getWorld() ) )
			{

				if( currentFurthestPoint < path.size() - 1 )
					currentFurthestPoint++;
				else
				{
					finalPath.add( path.get( currentFurthestPoint ) );
					break;
				}
			}
			else
			{
				finalPath.add( path.get( currentFurthestPoint ) );
				currentIndex = currentFurthestPoint++;

				if( currentFurthestPoint == path.size() - 1 )
				{
					finalPath.add( path.get( currentFurthestPoint ) );
					break;
				}
			}

		}
		return finalPath;
	}

	private double computeCost( PathNode currentNode, Pair<Integer, Integer> newNode )
	{
		return( currentNode.getCost() + distance( currentNode.getCurrentNode().getFirst(),
				currentNode.getCurrentNode().getSecond(), newNode.getFirst(), newNode.getSecond() ) );
	}

	private ArrayList<Pair<Integer, Integer>> getPath( ArrayList<PathNode> closedList )
	{

		ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
		PathNode element = closedList.get( closedList.size() - 1 );

		while( element.getParent() != null )
		{
			path.add( 0, element.getCurrentNode() );
			element = getElement( closedList, element.getParent() );
		}
		path.add( 0, element.getCurrentNode() );
		return path;
	}

	private PathNode getElement( ArrayList<PathNode> closedList, Pair<Integer, Integer> parent )
	{
		double minCost = Double.MAX_VALUE;
		PathNode nodeWithMinCost = null;
		for( PathNode node : closedList )
		{
			if( node.getCurrentNode().equal( parent ) && node.getCost() < minCost )
			{
				minCost = node.getCost();
				nodeWithMinCost = node;
			}
		}
		return nodeWithMinCost;
	}

	private boolean contains( ArrayList<PathNode> list, Pair<Integer, Integer> first,
			Pair<Integer, Integer> second, double computeCost )
	{
		for( PathNode node : list )
		{
			if( node.getParent() != null )
				if( node.getCurrentNode().equal( second ) && node.getParent().equal( first ) )
					return true;
				else if( node.getCurrentNode().equal( second ) && !node.getParent().equal( first )
						&& computeCost < node.getCost() )
				{

					node.setCost( computeCost );
					node.setParent( first );

					return true;
				}

		}
		return false;
	}

	private PathNode pointWithLowestCost( ArrayList<PathNode> list, float xGoal, float yGoal,
			World world )
	{
		double shortestDist = Double.MAX_VALUE;

		PathNode shortestPoint = null;
		for( int i = 0; i < list.size(); i++ )
		{

			double dist = list.get( i ).getCost()
					+ distance( list.get( i ).getCurrentNode().getFirst(), list.get( i )
							.getCurrentNode().getSecond(), ( int ) xGoal, ( int ) yGoal );
			if( dist < shortestDist )
			{
				shortestPoint = list.get( i );
				shortestDist = dist;

			}
		}
		return shortestPoint;

	}

	private static double distance( Integer x1, Integer y1, Integer x2, Integer y2 )
	{
		return Math.sqrt( Math.pow( ( x2 - x1 ), 2 ) + Math.pow( ( y2 - y1 ), 2 ) );
	}

	private static boolean isThereAnyCollision( Integer currentX, Integer currentY, Integer xGoal,
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
		// double normalLength = Math.sqrt((xGoal - currentX) * (xGoal - currentX) + (yGoal -
		// currentY) * (yGoal - currentY));
		// return Math.abs((xObj - currentX) * (yGoal - currentY) - (yObj - currentY) * (xGoal -
		// currentX)) / normalLength;
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
