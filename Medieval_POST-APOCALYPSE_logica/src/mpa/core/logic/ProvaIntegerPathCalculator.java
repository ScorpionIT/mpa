package mpa.core.logic;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.maths.MyMath;

public class ProvaIntegerPathCalculator
{

	private static float increment = 30;

	public ArrayList<Vector2f> computePath( World world, float xGoal, float yGoal, float xPlayer,
			float yPlayer )
	{

		if( xGoal < 0 || xGoal > world.getWidth() || yGoal < 0 || yGoal > world.getHeight() )
		{
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println( "mi hai dato un goal sbagliato " );
			System.out.println();
			System.out.println();
			System.out.println();
		}
		ArrayList<PathNode> openList = new ArrayList<>();
		ArrayList<PathNode> closedList = new ArrayList<>();

		PathNode currentPosition = new PathNode( null, new Pair<Integer, Integer>( ( int ) xPlayer,
				( int ) yPlayer ), 0 );
		Pair<Float, Float> currentVector = new Pair<Float, Float>( ( xGoal - xPlayer ),
				( yGoal - yPlayer ) );
		// int greatestCommonDivisor = MyMath.greatestCommonDivisor( currentVector.getFirst()
		// .intValue(), currentVector.getSecond().intValue() );
		// currentVector.setFirst( currentVector.getFirst() / greatestCommonDivisor );
		// currentVector.setSecond( currentVector.getSecond() / greatestCommonDivisor );
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
				// closedList.remove( currentPosition );
				closedList.add( new PathNode( currentPosition.getCurrentNode(), goal, computeCost(
						currentPosition, goal ) ) );
				break;
			}

			/* TODO use geometry to make the shit up here work */
			if( MyMath.distanceInteger( currentPosition.getCurrentNode().getFirst(),
					currentPosition.getCurrentNode().getSecond(), ( int ) xGoal, ( int ) yGoal ) < increment )
			{
				Pair<Integer, Integer> point = currentPosition.getCurrentNode();
				if( point.getFirst() < 0 || point.getFirst() > world.getWidth()
						|| point.getSecond() < 0 || point.getSecond() > world.getHeight() )
				{
					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println( "abbiamo trovato il problema? " );
					System.out.println();
					System.out.println();
					System.out.println();
				}
				Pair<Integer, Integer> goal = new Pair<Integer, Integer>( ( int ) xGoal,
						( int ) yGoal );
				closedList.add( new PathNode( currentPosition.getCurrentNode(), goal, computeCost(
						currentPosition, goal ) ) );
				break;

			}

			ArrayList<Pair<Float, Float>> points = new ArrayList<>();

			// Pair<Integer, Integer> currentNodePair = currentPosition.getCurrentNode();
			// currentVector.setFirst( -currentNodePair.getFirst() + xGoal );
			// currentVector.setSecond( -currentNodePair.getSecond() + yGoal );
			// greatestCommonDivisor = MyMath.greatestCommonDivisor( currentVector.getFirst()
			// .intValue(), currentVector.getSecond().intValue() );
			// currentVector.setFirst( currentVector.getFirst() / greatestCommonDivisor );
			// currentVector.setSecond( currentVector.getSecond() / greatestCommonDivisor );

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
			// normalVector.setFirst( currentVector.getSecond() );
			// normalVector.setSecond( -currentVector.getFirst() );
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					- increment * normalVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() - increment * normalVector.getSecond() ) );

			// ovest
			// normalVector.setFirst( -currentVector.getSecond() );
			// normalVector.setSecond( currentVector.getFirst() );
			points.add( new Pair<Float, Float>( currentPosition.getCurrentNode().getFirst()
					+ increment * normalVector.getFirst(), currentPosition.getCurrentNode()
					.getSecond() + increment * normalVector.getSecond() ) );

			for( Pair<Float, Float> pair : points )
			{
				// if( pair.getFirst() > world.getWidth() || pair.getFirst() < 0
				// || pair.getSecond() < 0 || pair.getSecond() > world.getHeight() )
				// {
				// continue;
				// }
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

							if( point.getFirst() < 0 || point.getFirst() > world.getWidth()
									|| point.getSecond() < 0
									|| point.getSecond() > world.getHeight() )
							{
								System.out.println();
								System.out.println();
								System.out.println();
								System.out
										.println( "sto aggiungendo qualcosa che non dovrei DDDDDDDDDDDDDDDDDD:" );
								System.out.println();
								System.out.println();
								System.out.println();
							}

						}
					}

				}
			}

		}

		ArrayList<Pair<Integer, Integer>> smartestPath = getSmartestPath( getPath( closedList ) );

		ArrayList<Vector2f> pathToReturn = new ArrayList<>();

		for( Pair<Integer, Integer> p : smartestPath )
		{
			Vector2f v2f = new Vector2f( p.getFirst().floatValue(), p.getSecond().floatValue() );
			pathToReturn.add( v2f );
			if( v2f.x > world.getWidth() || v2f.x < 0 || v2f.y < 0 || v2f.y > world.getHeight() )
			{
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println( "sto aggiungendo qualcosa che non dovrei :/" );
				System.out.println( "x=" + v2f.x + ", y=" + v2f.y );
				System.out.println();
				System.out.println();
				System.out.println();
			}
		}

		// System.out.println();
		// System.out.println();
		// System.out.println();
		// System.out.println( "closedList:" );
		// for( PathNode p : closedList )
		// {
		// System.out.println( p.getCurrentNode().getFirst() + " "
		// + p.getCurrentNode().getSecond() );
		// }
		//
		// System.out.println( "path to return" );
		// for( Vector2f v : pathToReturn )
		// {
		// System.out.println( v.x + " " + v.y );
		// }
		pathToReturn.remove( pathToReturn.size() - 1 );
		pathToReturn.add( new Vector2f( xGoal, yGoal ) );

		// System.out.println();

		return pathToReturn;

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
		return( currentNode.getCost() + MyMath.distanceInteger( currentNode.getCurrentNode()
				.getFirst(), currentNode.getCurrentNode().getSecond(), newNode.getFirst(), newNode
				.getSecond() ) );
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
					+ MyMath.distanceInteger( list.get( i ).getCurrentNode().getFirst(),
							list.get( i ).getCurrentNode().getSecond(), ( int ) xGoal,
							( int ) yGoal );
			if( dist < shortestDist )
			{
				shortestPoint = list.get( i );
				shortestDist = dist;

			}
		}
		return shortestPoint;

	}

}
