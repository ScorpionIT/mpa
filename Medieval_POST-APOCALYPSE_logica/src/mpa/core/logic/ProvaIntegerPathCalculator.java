package mpa.core.logic;

import java.util.ArrayList;

import mpa.core.maths.MyMath;

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
			if( !MyMath.isThereAnyCollision( currentPosition.getCurrentNode().getFirst(),
					currentPosition.getCurrentNode().getSecond(), new Integer( ( int ) xGoal ),
					new Integer( ( int ) yGoal ), world ) )
			{
				Pair<Integer, Integer> goal = new Pair<Integer, Integer>( ( int ) xGoal,
						( int ) yGoal );
				closedList.add( new PathNode( currentPosition.getCurrentNode(), goal, computeCost(
						currentPosition, goal ) ) );
				break;
			}

			/* TODO use geometry to make the shit up here work */
			if( MyMath.distanceInteger( currentPosition.getCurrentNode().getFirst(), currentPosition
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
				if( pair.getFirst() > world.getWidth() || pair.getFirst() < 0
						|| pair.getSecond() < 0 || pair.getSecond() > world.getHeight() )
				{
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println( "conitnue lalalalalalalalal" );
					System.out.println();
					System.out.println();
					System.out.println();
					continue;
				}
				Pair<Integer, Integer> point = new Pair<Integer, Integer>( new Integer( pair
						.getFirst().intValue() ), new Integer( pair.getSecond().intValue() ) );
				double computeCost = computeCost( currentPosition, point );

				if( !MyMath.isThereAnyCollision( currentPosition.getCurrentNode().getFirst(),
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
			if( !MyMath.isThereAnyCollision( currentPair.getFirst(), currentPair.getSecond(),
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
		return( currentNode.getCost() + MyMath.distanceInteger( currentNode.getCurrentNode().getFirst(),
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
					+ MyMath.distanceInteger( list.get( i ).getCurrentNode().getFirst(), list.get( i )
							.getCurrentNode().getSecond(), ( int ) xGoal, ( int ) yGoal );
			if( dist < shortestDist )
			{
				shortestPoint = list.get( i );
				shortestDist = dist;

			}
		}
		return shortestPoint;

	}

}
