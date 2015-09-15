//package mpa.core.logic;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class LongRangePathCalculator implements PathCalculator
//{
//
//	private static float increment = 30;
//
//	// @Override
//	// public ArrayList<Pair<Float, Float>> computePath(World world, Float xGoal, Float
//	// yGoal, Float
//	// xPlayer, Float yPlayer)
//	// {
//	// ArrayList<Pair<Float, Float>> path = new ArrayList<Pair<Float, Float>>();
//	// Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
//	// Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 1000,
//	// (yGoal -
//	// yPlayer) / 1000);
//	// path.add(currentPosition);
//	//
//	// while (path.size() < 50000)
//	// {
//	// if (path.get(path.size() - 1) != currentPosition && currentPosition != null)
//	// {
//	// path.add(currentPosition);
//	//
//	// }
//	// ArrayList<Pair<Float, Float>> points = new ArrayList<>();
//	// if (xGoal == currentPosition.getFirst() && yGoal == currentPosition.getSecond())
//	// break;
//	//
//	// Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
//	// Point2D.Float pointFloat = new Point2D.Float(currentPosition.getFirst().FloatValue(),
//	// currentPosition.getSecond().FloatValue());
//	// if (rect.contains(pointFloat))
//	// {
//	// break;
//	// }
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
//	// currentVector.getFirst(), currentPosition.getSecond()
//	// + increment * currentVector.getSecond()));
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
//	// currentVector.getFirst(), currentPosition.getSecond()
//	// - increment * currentVector.getSecond()));
//	//
//	// Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(),
//	// currentVector.getFirst());
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
//	// normalVector.getFirst(), currentPosition.getSecond()
//	// - increment * normalVector.getSecond()));
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
//	// normalVector.getFirst(), currentPosition.getSecond()
//	// + increment * normalVector.getSecond()));
//	//
//	// double shortestDist = Double.MAX_VALUE;
//	//
//	// Pair<Float, Float> shortestPoint = null;
//	// int indexShortest = 0;
//	// for (int i = 0; i < points.size(); i++)
//	// {
//	//
//	// double dist = distance(points.get(i).getFirst(), points.get(i).getSecond(), xGoal, yGoal);
//	// if (dist < shortestDist && !isThereAnyCollision(points.get(i).getFirst(),
//	// points.get(i).getSecond(), world)
//	// && !path.contains(points.get(i)))
//	// {
//	// // System.out.println("eccolo la dimensione del path attuale è " + path.size());
//	// shortestPoint = points.get(i);
//	// shortestDist = dist;
//	// indexShortest = i;
//	//
//	// }
//	// }
//	// if (increment == 0)
//	// break;
//	// if (shortestPoint == null)
//	// {
//	// System.err.println("nullllllllllllllllllllllllllloooooooooooooooooooooooooooooooooo");
//	// // currentPosition = path.get(path.size() - 2);
//	// increment -= 10;
//	// }
//	// else
//	// {
//	// increment = 50;
//	// currentPosition = shortestPoint;
//	// }
//	//
//	// if (indexShortest == 3 || indexShortest == 2)
//	// {
//	// currentVector = normalVector;
//	// }
//	//
//	// }
//	//
//	// increment = 50;
//	// return path;
//	//
//	// }
//
//	// @Override
//	// public ArrayList<Pair<Float, Float>> computePath(World world, Float xGoal, Float
//	// yGoal, Float
//	// xPlayer, Float yPlayer)
//	// {
//	// ArrayList<Pair<Float, Float>> openList = new ArrayList<Pair<Float, Float>>();
//	//
//	// ArrayList<Pair<Float, Float>> closedList = new ArrayList<Pair<Float, Float>>();
//	// Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
//	// Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 1000,
//	// (yGoal -
//	// yPlayer) / 1000);
//	// openList.add(currentPosition);
//	// while (openList.size() != 0)
//	// {
//	// currentPosition = pointWithLowestCost(openList, xGoal, yGoal, world);
//	// if (currentPosition == null)
//	// System.out.println("sono null");
//	//
//	// if (distance(currentPosition.getFirst(), currentPosition.getSecond(), xGoal, yGoal) <
//	// increment)
//	// {
//	// break;
//	// }
//	// // Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
//	// // Point2D.Float pointFloat = new
//	// // Point2D.Float(currentPosition.getFirst().FloatValue(),
//	// // currentPosition.getSecond().FloatValue());
//	// // if (rect.contains(pointFloat))
//	// // {
//	// // break;
//	// // }
//	//
//	// System.out.println();
//	// closedList.add(currentPosition);
//	// openList.remove(currentPosition);
//	//
//	// ArrayList<Pair<Float, Float>> points = new ArrayList<>();
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
//	// currentVector.getFirst(), currentPosition.getSecond()
//	// + increment * currentVector.getSecond()));
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
//	// currentVector.getFirst(), currentPosition.getSecond()
//	// - increment * currentVector.getSecond()));
//	//
//	// Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(),
//	// currentVector.getFirst());
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment *
//	// normalVector.getFirst(), currentPosition.getSecond()
//	// - increment * normalVector.getSecond()));
//	//
//	// points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment *
//	// normalVector.getFirst(), currentPosition.getSecond()
//	// + increment * normalVector.getSecond()));
//	//
//	// for (Pair<Float, Float> pair : points)
//	// {
//	// if (!contains(openList, pair.getFirst(), pair.getSecond()) && !contains(closedList,
//	// pair.getFirst(), pair.getSecond())
//	// && !isThereAnyCollision(pair.getFirst(), pair.getSecond(), world))
//	// {
//	// openList.add(pair);
//	// System.out.println("sto aggiungendo alla open list il punto " + pair.getFirst() + " " +
//	// pair.getSecond());
//	// }
//	// }
//	//
//	// }
//	// return closedList;
//	// }
//
//	ArrayList<Pair<Float, Float>> getPath(ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> closedList)
//	{
//		ArrayList<Pair<Float, Float>> path = new ArrayList<>();
//		Pair<Pair<Float, Float>, Pair<Float, Float>> element = closedList.get(closedList.size() - 1);
//
//		while (element.getFirst() != null)
//		{
//			System.out.println("sono nel while finale");
//			System.out.println(element.getSecond());
//			path.add(0, element.getSecond());
//			element = getElement(closedList, element.getFirst());
//			if (element == null)
//				System.out.println("sono null e sono element");
//		}
//		path.add(0, element.getSecond());
//		return path;
//	}
//
//	Pair<Pair<Float, Float>, Pair<Float, Float>> getElement(ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> closedList,
//			Pair<Float, Float> parent)
//	{
//		for (Pair<Pair<Float, Float>, Pair<Float, Float>> pair : closedList)
//		{
//			if (pair.getSecond().equal(parent))
//			{
//				return pair;
//			}
//		}
//		return null;
//	}
//
//	boolean contains(ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> openList, Pair<Float, Float> first, Pair<Float, Float> second)
//	{
//		for (Pair<Pair<Float, Float>, Pair<Float, Float>> pair : openList)
//		{
//			if (pair.getFirst() != null)
//				if (/* pair.getFirst().equal(first) && */pair.getSecond().equal(second))
//				{
//					System.out.println("pair " + pair.getFirst() + " | " + pair.getSecond() + " è uguale a " + first + " | " + second);
//
//					return true;
//				}
//
//		}
//		return false;
//	}
//
//	Pair<Pair<Float, Float>, Pair<Float, Float>> pointWithLowestCost(ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> list, Float xGoal,
//			Float yGoal, World world)
//	{
//		double shortestDist = Double.MAX_VALUE;
//
//		Pair<Pair<Float, Float>, Pair<Float, Float>> shortestPoint = null;
//		for (int i = 0; i < list.size(); i++)
//		{
//
//			double dist = distance(list.get(i).getSecond().getFirst(), list.get(i).getSecond().getSecond(), xGoal, yGoal);
//			if (dist < shortestDist)
//			{
//				shortestPoint = list.get(i);
//				shortestDist = dist;
//
//			}
//		}
//		return shortestPoint;
//
//	}
//
//	private static double distance(Float x1, Float y1, Float x2, Float y2)
//	{
//		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
//	}
//
//	private static boolean isThereAnyCollision(Float nextX, Float nextY, World world)
//	{
//		ArrayList<AbstractObject> objectsX = world.getObjectsXInTheRange(nextX);
//		ArrayList<AbstractObject> objectsY = world.getObjectsYInTheRange(nextY);
//
//		ArrayList<AbstractObject> intersection = new ArrayList<>();
//
//		for (AbstractObject objectX : objectsX)
//		{
//			Iterator<AbstractObject> it = objectsY.iterator();
//			while (it.hasNext())
//			{
//				AbstractObject objectY = it.next();
//				if (objectX == objectY)
//				{
//					intersection.add(objectX);
//					it.remove();
//				}
//			}
//		}
//
//		for (AbstractObject object : intersection)
//		{
//			if (distance(object.getX(), object.getY(), nextX, nextY) - object.getCollisionRay() <= 0)
//			{
//				return true;
//			}
//		}
//		return false;
//
//	}
//
//	@Override
//	public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float xPlayer, float yPlayer)
//	{
//		System.out.println("sono io ciao");
//		ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> openList = new ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>>();
//
//		ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>> closedList = new ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>>();
//		Pair<Pair<Float, Float>, Pair<Float, Float>> currentPosition = new Pair<Pair<Float, Float>, Pair<Float, Float>>(null, new Pair<Float, Float>(
//				xPlayer, yPlayer));
//		Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 500, (yGoal - yPlayer) / 500);
//		Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());
//
//		openList.add(currentPosition);
//		while (openList.size() != 0)
//		{
//			currentPosition = pointWithLowestCost(openList, xGoal, yGoal, world);
//			// currentVector = new Pair<Float, Float>((xGoal -
//			// currentPosition.getSecond().getFirst()) / 1000, (yGoal - currentPosition.getSecond()
//			// .getSecond()) / 1000);
//
//			if (currentPosition == null)
//				System.out.println("sono null");
//
//			System.out.println();
//			closedList.add(currentPosition);
//			openList.remove(currentPosition);
//			if (distance(currentPosition.getSecond().getFirst(), currentPosition.getSecond().getSecond(), xGoal, yGoal) < increment)
//			{
//				closedList.add(new Pair<Pair<Float, Float>, Pair<Float, Float>>(currentPosition.getSecond(), new Pair<Float, Float>(xGoal, yGoal)));
//				break;
//			}
//
//			ArrayList<Pair<Float, Float>> points = new ArrayList<>();
//
//			// nord
//			points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() + increment * currentVector.getFirst(), currentPosition
//					.getSecond().getSecond() + increment * currentVector.getSecond()));
//
//			// sud
//			points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() - increment * currentVector.getFirst(), currentPosition
//					.getSecond().getSecond() - increment * currentVector.getSecond()));
//
//			normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());
//			// est
//			points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() - increment * normalVector.getFirst(), currentPosition
//					.getSecond().getSecond() - increment * normalVector.getSecond()));
//
//			// ovest
//			points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() + increment * normalVector.getFirst(), currentPosition
//					.getSecond().getSecond() + increment * normalVector.getSecond()));
//
//			// // nord est
//			// points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() -
//			// increment
//			// * normalVector.getFirst(), currentPosition
//			// .getSecond().getSecond() + increment * currentVector.getSecond()));
//			// // nord ovest
//			// points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() +
//			// increment
//			// * normalVector.getFirst(), currentPosition
//			// .getSecond().getSecond() + increment * currentVector.getSecond()));
//			//
//			// // sud est
//			// points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() -
//			// increment
//			// * normalVector.getFirst(), currentPosition
//			// .getSecond().getSecond() - increment * currentVector.getSecond()));
//			// // sud ovest
//			// points.add(new Pair<Float, Float>(currentPosition.getSecond().getFirst() +
//			// increment
//			// * normalVector.getFirst(), currentPosition
//			// .getSecond().getSecond() - increment * currentVector.getSecond()));
//
//			int i = 0;
//			int index = 0;
//			for (Pair<Float, Float> pair : points)
//			{
//				if (!contains(openList, currentPosition.getSecond(), pair) && !contains(closedList, currentPosition.getSecond(), pair)
//						&& !isThereAnyCollision(pair.getFirst(), pair.getSecond(), world))
//				{
//					openList.add(new Pair<Pair<Float, Float>, Pair<Float, Float>>(currentPosition.getSecond(), pair));
//					System.out.println("sto aggiungendo alla open list il punto " + pair.getFirst() + " " + pair.getSecond());
//					index = i;
//				}
//				i++;
//			}
//
//		}
//
//		return getPath(closedList);
//	}
//
// }
