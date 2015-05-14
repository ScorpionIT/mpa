package mpa.core.logic;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.maths.MyMath;

public class PathCalculatorImplementation
{
	private ArrayList<MyPathNode> openList = new ArrayList<>();
	private ArrayList<MyPathNode> closedList = new ArrayList<>();
	private World world = GameManager.getInstance().getWorld();
	private Vector2f startingPoint;
	private Vector2f goal;
	private float increment = 30;

	public PathCalculatorImplementation( Vector2f startingPoint, Vector2f goal )
	{
		super();
		this.startingPoint = startingPoint;
		this.goal = goal;
	}

	public ArrayList<Vector2f> computePath()
	{

		MyPathNode currentPosition = new MyPathNode( null, startingPoint, 0 );
		openList.add( currentPosition );

		Vector2f currentVector = new Vector2f( -startingPoint.x + goal.x, -startingPoint.y + goal.y );
		int greatestCommonDivisor = MyMath.greatestCommonDivisor( ( int ) currentVector.x,
				( int ) currentVector.y );

		currentVector.set( currentVector.x / greatestCommonDivisor, currentVector.y
				/ greatestCommonDivisor );

		Vector2f normalVector = new Vector2f();

		// int iteration = 1;
		while( !openList.isEmpty() )
		{
			// System.out.println( "all'iterazione " + iteration++ + " la size della openList è "
			// + openList.size() );
			currentPosition = bestPoint();
			addToClosedList( currentPosition );

			Vector2f currentPoint = currentPosition.getArrival();
			ArrayList<AbstractObject> collisions = world.checkForCollisionInTheRange(
					currentPoint.x, goal.x, currentPoint.y, goal.y );

			if( collisions.isEmpty() )
			{
				closedList.add( new MyPathNode( currentPoint, goal, computeCost( currentPosition,
						goal ) ) );
				break;
			}

			currentVector.set( -currentPoint.x + goal.x, -currentPoint.y + goal.y );
			greatestCommonDivisor = MyMath.greatestCommonDivisor( ( int ) currentVector.x,
					( int ) currentVector.y );
			currentVector.set( currentVector.x / greatestCommonDivisor, currentVector.y
					/ greatestCommonDivisor );

			// North
			Vector2f point = new Vector2f( currentPoint.x + currentVector.x * increment,
					currentPoint.y + currentVector.y * increment );
			addToOpenList( new MyPathNode( currentPoint, point,
					computeCost( currentPosition, point ) ) );

			// South
			point = new Vector2f( currentPoint.x - currentVector.x * increment, currentPoint.y
					- currentVector.y * increment );
			addToOpenList( new MyPathNode( currentPoint, point,
					computeCost( currentPosition, point ) ) );

			// East
			normalVector.set( currentVector.y, -currentVector.x );
			point = new Vector2f( currentPoint.x + normalVector.x * increment, currentPoint.y
					+ normalVector.y * increment );
			addToOpenList( new MyPathNode( currentPoint, point,
					computeCost( currentPosition, point ) ) );

			// West
			point = new Vector2f( currentPoint.x - normalVector.x * increment, currentPoint.y
					- normalVector.y * increment );
			addToOpenList( new MyPathNode( currentPoint, point,
					computeCost( currentPosition, point ) ) );

		}

		System.out.println();
		System.out.println();
		System.err.println( "la size della closedList è " + closedList.size() );
		System.out.println();
		System.out.println();

		return getPath();
	}

	private ArrayList<Vector2f> getPath()
	{

		ArrayList<Vector2f> path = new ArrayList<>();

		if( closedList.size() == 2 )
		{
			path.add( startingPoint );
			path.add( goal );
			return path;
		}

		Vector2f currentPoint = null;

		Vector2f arrival = closedList.get( closedList.size() - 1 ).getArrival();

		if( ( ( int ) arrival.x ) == ( ( int ) goal.x )
				&& ( ( int ) arrival.y ) == ( ( int ) goal.y ) )
			currentPoint = arrival;
		else
		{
			for( MyPathNode node : closedList )
			{
				arrival = node.getArrival();
				if( ( ( int ) arrival.x ) == ( ( int ) goal.x )
						&& ( ( int ) arrival.y ) == ( ( int ) goal.y ) )
				{
					currentPoint = arrival;
					break;
				}
			}
		}

		path.add( currentPoint );

		System.out.println();
		System.out.println();
		System.out.print( "sono prima del for e currentPoint è " );
		if( currentPoint == null )
			System.out.println( " null " );
		else
			System.out.println( " non null" );
		System.out.println();
		System.out.println();

		for( MyPathNode node : closedList )
		{
			arrival = node.getArrival();

			if( ( ( int ) arrival.x ) == ( ( int ) currentPoint.x )
					&& ( ( int ) arrival.y ) == ( ( int ) currentPoint.y ) )
			{
				Vector2f newStartingPoint = node.getStartingPoint();

				if( newStartingPoint == null )
					break;
				else
				{
					path.add( 0, newStartingPoint );
					currentPoint = newStartingPoint;
					// closedList.remove( node );
				}
			}
		}
		return path;

		// Vector2f furthestPoint = path.get( 0 );
		// int index = 0;
		//
		// ArrayList<Vector2f> smartestPath = new ArrayList<>();
		// smartestPath.add( startingPoint );
		// path.remove( startingPoint );
		//
		// System.out.println( "sono uscito dal for? entro nel while" );
		//
		// while( furthestPoint != path.get( path.size() - 1 ) )
		// {
		// System.out.println( index );
		// Vector2f pointToCheck = path.get( index++ );
		// if( world.checkForCollisionInTheRange( furthestPoint.x, pointToCheck.x,
		// furthestPoint.y, pointToCheck.y ).isEmpty() )
		// furthestPoint = pointToCheck;
		// else
		// smartestPath.add( furthestPoint );
		// }
		// return smartestPath;
	}

	private void addToOpenList( MyPathNode currentNode )
	{
		Vector2f point = currentNode.getArrival();
		if( isOutOfTheWorld( point.x, point.y ) )
			return;

		if( !world.checkForCollision( point.x, point.y ).isEmpty() )
			return;

		for( MyPathNode node : closedList )
		{
			Vector2f arrival = node.getArrival();
			if( ( ( int ) arrival.x ) == ( ( int ) point.x )
					&& ( ( int ) arrival.y ) == ( ( int ) point.y ) )
				return;
		}

		openList.add( currentNode );
	}

	private boolean isOutOfTheWorld( float x, float y )
	{
		float width = GameManager.getInstance().getWorld().getWidth();
		float height = GameManager.getInstance().getWorld().getHeight();

		return( x > width || x < 0 || y > height || y < 0 );
	}

	private void addToClosedList( MyPathNode newNode )
	{
		// Vector2f newArrival = newNode.getArrival();
		// Vector2f startingPoint = newNode.getStartingPoint();
		//
		// if( startingPoint == null )
		// {
		// closedList.add( newNode );
		// return;
		// }
		//
		// for( MyPathNode node : closedList )
		// {
		// Vector2f arrival = node.getArrival();
		// if( ( ( int ) arrival.x ) == ( ( int ) newArrival.x )
		// && ( ( int ) arrival.y ) == ( ( int ) newArrival.y ) )
		// if( node.getCost() > newNode.getCost() )
		// {
		// node.setStartingPoint( startingPoint );
		// node.setCost( newNode.getCost() );
		// return;
		// }
		// }

		closedList.add( newNode );
		openList.remove( newNode );

	}

	private MyPathNode bestPoint()
	{
		MyPathNode bestNode = null;

		float minCost = Float.MAX_VALUE;

		for( MyPathNode node : openList )
		{
			Vector2f arrival = node.getArrival();
			float cost = node.getCost()
					+ MyMath.distanceFloat( arrival.x, arrival.y, goal.x, goal.y );
			if( minCost > cost )
			{
				minCost = cost;
				bestNode = node;
			}
		}
		return bestNode;
	}

	private float computeCost( MyPathNode currentNode, Vector2f point )
	{

		return currentNode.getCost() + MyMath.distanceFloat( point.x, point.y, goal.x, goal.y );

	}
}
