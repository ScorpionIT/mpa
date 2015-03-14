//package mpa.core.logic;
//
//import java.awt.geom.Line2D;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class ProvaPathCalculator implements PathCalculator
//{
//
//	private static float increment = 40;
//
//	@Override
//	public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float xPlayer, float yPlayer)
//	{
//
//		ArrayList<PathNode> openList = new ArrayList<>();
//
//		ArrayList<PathNode> closedList = new ArrayList<>();
//		PathNode currentPosition = new PathNode(null, new Pair<Float, Float>(xPlayer, yPlayer), 0);
//		Pair<Float, Float> currentVector = new Pair<Float, Float>((xGoal - xPlayer) / 500, (yGoal - yPlayer) / 500);
//		Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());
//
//		openList.add(currentPosition);
//		while (openList.size() != 0)
//		{
//			currentPosition = pointWithLowestCost(openList, xGoal, yGoal, world);
//
//			currentVector = new Pair<Float, Float>((xGoal - currentPosition.getCurrentNode().getFirst()) / 500, (yGoal - currentPosition
//					.getCurrentNode().getSecond()) / 500);
//
//			// if (currentPosition == null)
//			// System.out.println("sono null");
//
//			// Rectangle2D.Float rect = new Rectangle2D.Float(xGoal - 10, yGoal - 10, 20, 20);
//			// Point2D.Float pointFloat = new
//			// Point2D.Float(currentPosition.getFirst().FloatValue(),
//			// currentPosition.getSecond().FloatValue());
//			// if (rect.contains(pointFloat))
//			// {
//			// break;
//			// }
//
//			System.out.println();
//			closedList.add(currentPosition);
//			openList.remove(currentPosition);
//			// if (!isThereAnyCollision(currentPosition.getCurrentNode().getFirst(),
//			// currentPosition.getCurrentNode().getSecond(), xGoal, yGoal, world))
//			// {
//			// Pair<Float, Float> goal = new Pair<Float, Float>(xGoal, yGoal);
//			// closedList.add(new PathNode(currentPosition.getCurrentNode(), goal,
//			// computeCost(currentPosition, goal)));
//			// break;
//			// }
//			if (distance(currentPosition.getCurrentNode().getFirst(), currentPosition.getCurrentNode().getSecond(), xGoal, yGoal) < increment)
//			{
//				Pair<Float, Float> goal = new Pair<Float, Float>(xGoal, yGoal);
//				closedList.add(new PathNode(currentPosition.getCurrentNode(), goal, computeCost(currentPosition, goal)));
//				break;
//			}
//
//			ArrayList<Pair<Float, Float>> points = new ArrayList<>();
//
//			// nord
//			points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() + increment * currentVector.getFirst(), currentPosition
//					.getCurrentNode().getSecond() + increment * currentVector.getSecond()));
//
//			// sud
//			points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() - increment * currentVector.getFirst(), currentPosition
//					.getCurrentNode().getSecond() - increment * currentVector.getSecond()));
//
//			normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());
//
//			// est
//			points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() - increment * normalVector.getFirst(), currentPosition
//					.getCurrentNode().getSecond() - increment * normalVector.getSecond()));
//
//			// ovest
//			points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() + increment * normalVector.getFirst(), currentPosition
//					.getCurrentNode().getSecond() + increment * normalVector.getSecond()));
//
//			// // nord est
//			// points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() -
//			// increment * normalVector.getFirst(), currentPosition
//			// .getCurrentNode().getSecond() + increment * currentVector.getSecond()));
//			// // nord ovest
//			// points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() +
//			// increment * normalVector.getFirst(), currentPosition
//			// .getCurrentNode().getSecond() + increment * currentVector.getSecond()));
//			//
//			// // sud est
//			// points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() -
//			// increment * normalVector.getFirst(), currentPosition
//			// .getCurrentNode().getSecond() - increment * currentVector.getSecond()));
//			// // sud ovest
//			// points.add(new Pair<Float, Float>(currentPosition.getCurrentNode().getFirst() +
//			// increment * normalVector.getFirst(), currentPosition
//			// .getCurrentNode().getSecond() - increment * currentVector.getSecond()));
//
//			for (Pair<Float, Float> pair : points)
//			{
//				double computeCost = computeCost(currentPosition, pair);
//				if (!isThereAnyCollision(pair.getFirst(), pair.getSecond(), world))
//				{
//					if (!contains(closedList, currentPosition.getCurrentNode(), pair, computeCost))
//					{
//						if (!contains(openList, currentPosition.getCurrentNode(), pair, computeCost))
//						{
//							openList.add(new PathNode(currentPosition.getCurrentNode(), pair, computeCost));
//							System.out.println("sto aggiungendo alla open list il punto " + pair.getFirst() + " " + pair.getSecond());
//						}
//					}
//
//				}
//			}
//
//		}
//
//		return getPath(closedList);
//
//	}
//
//	private double computeCost(PathNode currentNode, Pair<Float, Float> newNode)
//	{
//		return (currentNode.getCost() + distance(currentNode.getCurrentNode().getFirst(), currentNode.getCurrentNode().getSecond(),
//				newNode.getFirst(), newNode.getSecond()));
//	}
//
//	private ArrayList<Pair<Float, Float>> getPath(ArrayList<PathNode> closedList)
//	{
//		for (PathNode pathNode : closedList)
//		{
//			System.out.println("Parent: " + pathNode.getParent() + " Node: " + pathNode.getCurrentNode() + " costo: " + pathNode.getCost());
//		}
//		ArrayList<Pair<Float, Float>> path = new ArrayList<>();
//		PathNode element = closedList.get(closedList.size() - 1);
//
//		while (element.getParent() != null)
//		{
//			path.add(0, element.getCurrentNode());
//			element = getElement(closedList, element.getParent());
//		}
//		path.add(0, element.getCurrentNode());
//		System.out.println("Size closedList " + closedList.size());
//		return path;
//	}
//
//	private PathNode getElement(ArrayList<PathNode> closedList, Pair<Float, Float> parent)
//	{
//		double minCost = Double.MAX_VALUE;
//		PathNode nodeWithMinCost = null;
//		for (PathNode node : closedList)
//		{
//			if (node.getCurrentNode().equal(parent) && node.getCost() < minCost)
//			{
//				minCost = node.getCost();
//				nodeWithMinCost = node;
//			}
//		}
//		return nodeWithMinCost;
//	}
//
//	private boolean contains(ArrayList<PathNode> list, Pair<Float, Float> first, Pair<Float, Float> second, double computeCost)
//	{
//		for (PathNode node : list)
//		{
//			if (node.getParent() != null)
//				if (node.getCurrentNode().equal(second) && node.getParent().equal(first))
//				{
//					return true;
//				}
//			if (node.getCurrentNode().equal(second) && !node.getParent().equal(first) && computeCost < node.getCost())
//			{
//
//				node.setCost(computeCost);
//				node.setParent(first);
//
//				System.out.println("pair " + node.getParent() + " | " + node.getCurrentNode() + " è uguale a " + first + " | " + second);
//
//				return true;
//			}
//
//		}
//		return false;
//	}
//
//	private PathNode pointWithLowestCost(ArrayList<PathNode> list, Float xGoal, Float yGoal, World world)
//	{
//		double shortestDist = Double.MAX_VALUE;
//
//		PathNode shortestPoint = null;
//		for (int i = 0; i < list.size(); i++)
//		{
//
//			double dist = list.get(i).getCost()
//					+ distance(list.get(i).getCurrentNode().getFirst(), list.get(i).getCurrentNode().getSecond(), xGoal, yGoal);
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
//				System.out.println(object.getCollisionRay());
//				return true;
//			}
//		}
//		return false;
//
//	}
//
//	private static boolean isThereAnyCollision(Float currentX, Float currentY, Float xGoal, Float yGoal, World world)
//	{
//		ArrayList<AbstractObject> objectsX;
//		ArrayList<AbstractObject> objectsY;
//		Line2D.Float line = new Line2D.Float(currentX.floatValue(), currentY.floatValue(), xGoal.floatValue(), yGoal.floatValue());
//		if (currentX < xGoal)
//			objectsX = world.getObjectsXInTheRange(currentX.intValue(), xGoal.intValue());
//		else
//			objectsX = world.getObjectsXInTheRange(xGoal.intValue(), currentX.intValue());
//
//		if (currentY < yGoal)
//			objectsY = world.getObjectsYInTheRange(currentY.intValue(), yGoal.intValue());
//		else
//			objectsY = world.getObjectsYInTheRange(yGoal.intValue(), currentY.intValue());
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
//		System.out.println("size intersersection " + intersection.size());
//		for (AbstractObject object : intersection)
//		{
//			System.out.println(line.getX1() + " " + line.getY1() + " " + line.getX2() + " " + line.getY2());
//			if (line.intersects((double) object.getX() - object.getWidth() / 2, (double) object.getY() - object.getHeight() / 2,
//					(double) object.getWidth(), (double) object.getHeight()))
//				return true;
//			// if (distance(object.getX(), object.getY(), currentX, currentY) -
//			// object.getCollisionRay() <= 0)
//			// {
//			// return true;
//			// }
//		}
//		return false;
//
//	}
// }
