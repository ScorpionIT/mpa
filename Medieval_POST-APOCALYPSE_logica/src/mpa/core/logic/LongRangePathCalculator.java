package mpa.core.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class LongRangePathCalculator implements PathCalculator
{

	private static float increment = 60;

	// @Override
	// public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float
	// xPlayer, float yPlayer)
	// {
	// ArrayList<Pair<Float, Float>> path = new ArrayList<Pair<Float, Float>>();
	// Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
	// Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 1000, (yGoal -
	// yPlayer) / 1000);
	// path.add(currentPosition);
	//
	// while (path.size() < 50000)
	// {
	// if (path.get(path.size() - 1) != currentPosition && currentPosition != null)
	// {
	// path.add(currentPosition);
	//
	// }
	// ArrayList<Pair<Float, Float>> points = new ArrayList<>();
	// if (xGoal == currentPosition.getFirst() && yGoal == currentPosition.getSecond())
	// break;
	//
	// Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
	// Point2D.Float pointFloat = new Point2D.Float(currentPosition.getFirst().floatValue(),
	// currentPosition.getSecond().floatValue());
	// if (rect.contains(pointFloat))
	// {
	// break;
	// }
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
	// currentVector.getFirst(), currentPosition.getSecond()
	// + increment * currentVector.getSecond()));
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
	// currentVector.getFirst(), currentPosition.getSecond()
	// - increment * currentVector.getSecond()));
	//
	// Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(),
	// currentVector.getFirst());
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
	// normalVector.getFirst(), currentPosition.getSecond()
	// - increment * normalVector.getSecond()));
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
	// normalVector.getFirst(), currentPosition.getSecond()
	// + increment * normalVector.getSecond()));
	//
	// double shortestDist = Double.MAX_VALUE;
	//
	// Pair<Float, Float> shortestPoint = null;
	// int indexShortest = 0;
	// for (int i = 0; i < points.size(); i++)
	// {
	//
	// double dist = distance(points.get(i).getFirst(), points.get(i).getSecond(), xGoal, yGoal);
	// if (dist < shortestDist && !isThereAnyCollision(points.get(i).getFirst(),
	// points.get(i).getSecond(), world)
	// && !path.contains(points.get(i)))
	// {
	// // System.out.println("eccolo la dimensione del path attuale è " + path.size());
	// shortestPoint = points.get(i);
	// shortestDist = dist;
	// indexShortest = i;
	//
	// }
	// }
	// if (increment == 0)
	// break;
	// if (shortestPoint == null)
	// {
	// System.err.println("nullllllllllllllllllllllllllloooooooooooooooooooooooooooooooooo");
	// // currentPosition = path.get(path.size() - 2);
	// increment -= 10;
	// }
	// else
	// {
	// increment = 50;
	// currentPosition = shortestPoint;
	// }
	//
	// if (indexShortest == 3 || indexShortest == 2)
	// {
	// currentVector = normalVector;
	// }
	//
	// }
	//
	// increment = 50;
	// return path;
	//
	// }

	// @Override
	// public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float
	// xPlayer, float yPlayer)
	// {
	// ArrayList<Pair<Float, Float>> openList = new ArrayList<Pair<Float, Float>>();
	//
	// ArrayList<Pair<Float, Float>> closedList = new ArrayList<Pair<Float, Float>>();
	// Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
	// Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 1000, (yGoal -
	// yPlayer) / 1000);
	// openList.add(currentPosition);
	// while (openList.size() != 0)
	// {
	// currentPosition = pointWithLowestCost(openList, xGoal, yGoal, world);
	// if (currentPosition == null)
	// System.out.println("sono null");
	//
	// if (distance(currentPosition.getFirst(), currentPosition.getSecond(), xGoal, yGoal) <
	// increment)
	// {
	// break;
	// }
	// // Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
	// // Point2D.Float pointFloat = new
	// // Point2D.Float(currentPosition.getFirst().floatValue(),
	// // currentPosition.getSecond().floatValue());
	// // if (rect.contains(pointFloat))
	// // {
	// // break;
	// // }
	//
	// System.out.println();
	// closedList.add(currentPosition);
	// openList.remove(currentPosition);
	//
	// ArrayList<Pair<Float, Float>> points = new ArrayList<>();
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
	// currentVector.getFirst(), currentPosition.getSecond()
	// + increment * currentVector.getSecond()));
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
	// currentVector.getFirst(), currentPosition.getSecond()
	// - increment * currentVector.getSecond()));
	//
	// Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(),
	// currentVector.getFirst());
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
	// normalVector.getFirst(), currentPosition.getSecond()
	// - increment * normalVector.getSecond()));
	//
	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
	// normalVector.getFirst(), currentPosition.getSecond()
	// + increment * normalVector.getSecond()));
	//
	// for (Pair<Float, Float> pair : points)
	// {
	// if (!contains(closedList, pair.getFirst(), pair.getSecond()) && !contains(closedList,
	// pair.getFirst(), pair.getSecond())
	// && !isThereAnyCollision(pair.getFirst(), pair.getSecond(), world))
	// {
	// openList.add(pair);
	// System.out.println("sto aggiungendo alla open list il punto " + pair.getFirst() + " " +
	// pair.getSecond());
	// }
	// }
	//
	// }
	// return closedList;
	// }

	@Override
	public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float xPlayer, float yPlayer)
	{
		ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> openList = new ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>>();

		ArrayList<Pair<Float, Float>> closedList = new ArrayList<Pair<Float, Float>>();
		Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
		Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 1000, (yGoal - yPlayer) / 1000);
		// openList.add(currentPosition); //TODO
		while (openList.size() != 0)
		{
			// currentPosition = pointWithLowestCost(openList, xGoal, yGoal, world); //TODO
			if (currentPosition == null)
				System.out.println("sono null");

			if (distance(currentPosition.getFirst(), currentPosition.getSecond(), xGoal, yGoal) < increment)
			{
				break;
			}
			// Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
			// Point2D.Float pointFloat = new
			// Point2D.Float(currentPosition.getFirst().floatValue(),
			// currentPosition.getSecond().floatValue());
			// if (rect.contains(pointFloat))
			// {
			// break;
			// }

			System.out.println();
			closedList.add(currentPosition);
			openList.remove(currentPosition);

			ArrayList<Pair<Float, Float>> points = new ArrayList<>();
			points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment * currentVector.getFirst(), currentPosition.getSecond()
					+ increment * currentVector.getSecond()));

			points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment * currentVector.getFirst(), currentPosition.getSecond()
					- increment * currentVector.getSecond()));

			Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());

			points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment * normalVector.getFirst(), currentPosition.getSecond()
					- increment * normalVector.getSecond()));

			points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment * normalVector.getFirst(), currentPosition.getSecond()
					+ increment * normalVector.getSecond()));

			for (Pair<Float, Float> pair : points)
			{
				if (!contains(closedList, pair.getFirst(), pair.getSecond()) && !contains(closedList, pair.getFirst(), pair.getSecond())
						&& !isThereAnyCollision(pair.getFirst(), pair.getSecond(), world))
				{
					// openList.add(pair); //TODO
					System.out.println("sto aggiungendo alla open list il punto " + pair.getFirst() + " " + pair.getSecond());
				}
			}

		}
		return closedList;
	}

	boolean contains(ArrayList<Pair<Float, Float>> list, Float x, Float y)
	{
		for (Pair<Float, Float> pair : list)
		{
			if (pair.getFirst().equals(x) && pair.getSecond().equals(y))
			{
				System.out.println("pair " + pair.getFirst() + " | " + pair.getSecond() + " è uguale a " + x + " | " + y);

				return true;
			}

		}
		return false;
	}

	Pair<Float, Float> pointWithLowestCost(ArrayList<Pair<Float, Float>> list, float xGoal, float yGoal, World world)
	{
		double shortestDist = Double.MAX_VALUE;

		Pair<Float, Float> shortestPoint = null;
		for (int i = 0; i < list.size(); i++)
		{

			double dist = distance(list.get(i).getFirst(), list.get(i).getSecond(), xGoal, yGoal);
			if (dist < shortestDist)
			{
				shortestPoint = list.get(i);
				shortestDist = dist;

			}
		}
		return shortestPoint;

	}

	private static double distance(float x1, float y1, float x2, float y2)
	{
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	private static boolean isThereAnyCollision(float nextX, float nextY, World world)
	{
		ArrayList<AbstractObject> objectsX = world.getObjectsXInTheRange(nextX);
		ArrayList<AbstractObject> objectsY = world.getObjectsYInTheRange(nextY);

		ArrayList<AbstractObject> intersection = new ArrayList<>();

		for (AbstractObject objectX : objectsX)
		{
			Iterator<AbstractObject> it = objectsY.iterator();
			while (it.hasNext())
			{
				AbstractObject objectY = it.next();
				if (objectX == objectY)
				{
					intersection.add(objectX);
					it.remove();
				}
			}
		}

		for (AbstractObject object : intersection)
		{
			if (distance(object.getX(), object.getY(), nextX, nextY) - object.getCollisionRay() <= 0)
			{
				return true;
			}
		}
		return false;

	}
}
