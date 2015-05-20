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

		MyPathNode currentPosition = null;
		openList.add( new MyPathNode( null, startingPoint, 0 ) );

		Vector2f currentVector = new Vector2f( -startingPoint.x + goal.x, -startingPoint.y + goal.y );

		currentVector = computeCurrentVector( startingPoint, goal );
		Vector2f normalVector = new Vector2f();

		while( !openList.isEmpty() )
		{
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

			currentVector = computeCurrentVector( currentPoint, goal );
			normalVector = computeNormalVector( currentVector );

			boolean noPointAdded = true;

			// North
			Vector2f point = new Vector2f( currentPoint.x + currentVector.x * increment,
					currentPoint.y + currentVector.y * increment );
			if( addToOpenList( new MyPathNode( currentPoint, point, computeCost( currentPosition,
					point ) ) ) )
				noPointAdded = false;

			// South
			point = new Vector2f( currentPoint.x - currentVector.x * increment, currentPoint.y
					- currentVector.y * increment );
			if( addToOpenList( new MyPathNode( currentPoint, point, computeCost( currentPosition,
					point ) ) ) )
				noPointAdded = false;

			// East
			point = new Vector2f( currentPoint.x + normalVector.x * increment, currentPoint.y
					+ normalVector.y * increment );
			if( addToOpenList( new MyPathNode( currentPoint, point, computeCost( currentPosition,
					point ) ) ) )
				noPointAdded = false;

			// West
			point = new Vector2f( currentPoint.x - normalVector.x * increment, currentPoint.y
					- normalVector.y * increment );
			if( addToOpenList( new MyPathNode( currentPoint, point, computeCost( currentPosition,
					point ) ) ) )
				noPointAdded = false;

			// if( !noPointAdded )
			// increment /= 2;
			// }

			if( noPointAdded )
			{
				// North-West
				point = new Vector2f( currentPoint.x + increment * ( currentPoint.x - increment ),
						currentPoint.y + increment * ( currentPoint.y + increment ) );
				if( addToOpenList( new MyPathNode( currentPoint, point, computeCost(
						currentPosition, point ) ) ) )
					noPointAdded = false;

				// North-East
				point = new Vector2f( currentPoint.x + increment * ( currentPoint.x + increment ),
						currentPoint.y + increment * ( currentPoint.y + increment ) );
				if( addToOpenList( new MyPathNode( currentPoint, point, computeCost(
						currentPosition, point ) ) ) )
					noPointAdded = false;

				// South-West
				point = new Vector2f( currentPoint.x + increment * ( currentPoint.x - increment ),
						currentPoint.y + increment * ( currentPoint.y - increment ) );
				if( addToOpenList( new MyPathNode( currentPoint, point, computeCost(
						currentPosition, point ) ) ) )
					noPointAdded = false;

				// South-East
				point = new Vector2f( currentPoint.x + increment * ( currentPoint.x + increment ),
						currentPoint.y + increment * ( currentPoint.y - increment ) );
				if( addToOpenList( new MyPathNode( currentPoint, point, computeCost(
						currentPosition, point ) ) ) )
					noPointAdded = false;

			}

			if( openList.isEmpty() )
			{
				increment /= 2;
				openList.add( currentPosition );
				closedList.remove( currentPosition );
			}
			else
				increment = 30;

		}

		return getPath();
	}

	private Vector2f computeCurrentVector( Vector2f start, Vector2f arrival )
	{
		Vector2f vector;

		Vector2f point = new Vector2f( -start.x + arrival.x, -start.y + arrival.y );

		if( Math.abs( point.x ) >= Math.abs( point.y ) )
			vector = new Vector2f( 1, point.y / point.x );
		else
			vector = new Vector2f( point.x / point.y, 1 );

		return vector;
	}

	private Vector2f computeNormalVector( Vector2f vector )
	{
		return new Vector2f( -vector.y, vector.x );
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

		MyPathNode currentNode = closedList.get( closedList.size() - 1 );
		Vector2f currentPoint = currentNode.getArrival();

		path.add( currentPoint );
		currentPoint = currentNode.getStartingPoint();

		int cont = 0;
		while( currentPoint != null )
		{
			int index = 0;
			for( MyPathNode node : closedList )
			{
				Vector2f tmp = node.getArrival();

				if( ( ( int ) tmp.x ) == ( ( int ) currentPoint.x )
						&& ( ( int ) tmp.y ) == ( ( int ) currentPoint.y ) )
				{
					path.add( 0, currentPoint );
					currentPoint = node.getStartingPoint();
					node = closedList.remove( index );
					break;
				}
				index++;
			}
		}
		currentPoint = path.get( 0 );
		Vector2f furthestPoint = path.get( 1 );
		int index = 2;

		ArrayList<Vector2f> smartestPath = new ArrayList<>();
		smartestPath.add( path.get( 0 ) );
		while( index < path.size() )
		{
			Vector2f pointToCheck = path.get( index++ );
			if( !world.checkForCollisionInTheRange( currentPoint.x, pointToCheck.x, currentPoint.y,
					pointToCheck.y ).isEmpty() )
			{
				smartestPath.add( furthestPoint );
				currentPoint = furthestPoint;
			}
			else
			{
				furthestPoint = pointToCheck;
			}
		}
		smartestPath.add( goal );
		System.out.println();
		System.out.println( "la size è " + smartestPath.size() );
		System.out.println();

		return smartestPath;
	}

	private boolean addToOpenList( MyPathNode currentNode )
	{

		Vector2f point = currentNode.getArrival();
		if( isOutOfTheWorld( point.x, point.y ) )
			return false;

		Vector2f start = currentNode.getStartingPoint();

		if( !world.checkForCollisionInTheRange( start.x, point.x, start.y, point.y ).isEmpty() )
			return false;

		for( MyPathNode node : closedList )
		{
			Vector2f arrival = node.getArrival();
			if( ( ( int ) arrival.x ) == ( ( int ) point.x )
					&& ( ( int ) arrival.y ) == ( ( int ) point.y ) )
				return false;
		}

		openList.add( currentNode );
		return true;
	}

	private boolean isOutOfTheWorld( float x, float y )
	{
		float width = world.getWidth();
		float height = world.getHeight();

		if( x > width || x < 0 || y > height || y < 0 )
			return true;
		return false;
	}

	private void addToClosedList( MyPathNode newNode )
	{
		closedList.add( newNode );
		openList.remove( newNode );
	}

	private MyPathNode bestPoint()
	{
		MyPathNode bestNode = null;

		float minCost = Float.MAX_VALUE;

		if( openList.size() == 1 )
			return openList.get( 0 );

		for( MyPathNode node : openList )
		{
			Vector2f arrival = node.getArrival();
			// float cost = node.getCost()
			// + MyMath.distanceFloat( arrival.x, arrival.y, goal.x, goal.y );
			// if( minCost > cost )
			// {
			// minCost = cost;
			// bestNode = node;
			// }

			float cost = MyMath.distanceFloat( arrival.x, arrival.y, goal.x, goal.y );

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
