package mpa.core.logic;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class LongRangePathCalculator implements PathCalculator
{

	private static final float increment = 20;

	// public LongRangePathCalculator()
	// {
	//
	// }

	@Override
	public ArrayList<Pair<Float, Float>> computePath(World world, float xGoal, float yGoal, float xPlayer, float yPlayer)
	{
		ArrayList<Pair<Float, Float>> path = new ArrayList<Pair<Float, Float>>();
		Pair<Float, Float> currentPosition = new Pair<Float, Float>(xPlayer, yPlayer);
		Pair<Float, Float> currentVector = new Pair<Float, Float>(xGoal - xPlayer, yGoal - yPlayer);

		int sign = 0;
		while (sign < 5)
		{
			path.add(currentPosition);
			ArrayList<Pair<Float, Float>> points = new ArrayList<>();
			if (xGoal == currentPosition.getFirst() && yGoal == currentPosition.getSecond())
			{
				System.out.println("Sono uguali uguali");
				break;
			}
			Rectangle2D.Float rect = new Rectangle2D.Float();
			rect.setRect(xGoal, yGoal, 40, 40);
			Point2D.Float pointFloat = new Point2D.Float(currentPosition.getFirst().floatValue(), currentPosition.getSecond().floatValue());
			System.out.println("sono current position " + currentPosition.getFirst() + " " + currentPosition.getSecond());
			if (rect.contains(pointFloat))
			{
				System.out.println("Non sono uguali uguali");
				break;
			}

			points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment * currentVector.getFirst(), currentPosition.getSecond()
					+ increment * currentVector.getSecond()));

			points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment * currentVector.getFirst(), currentPosition.getSecond()
					- increment * currentVector.getSecond()));

			Pair<Float, Float> normalVector = new Pair<Float, Float>(-currentVector.getSecond(), currentVector.getFirst());

			points.add(new Pair<Float, Float>(currentPosition.getFirst() - increment * normalVector.getFirst(), currentPosition.getSecond()
					- increment * normalVector.getSecond()));

			points.add(new Pair<Float, Float>(currentPosition.getFirst() + increment * normalVector.getFirst(), currentPosition.getSecond()
					+ increment * normalVector.getSecond()));

			double shortestDist = distance(points.get(0).getFirst(), points.get(0).getSecond(), xGoal, yGoal);

			System.out.println("sono shortest dist " + shortestDist);
			Pair<Float, Float> shortestPoint = points.get(0);
			int i = 0;
			int indexShortest = 0;
			for (Pair<Float, Float> pair : points)
			{
				System.out.println(pair.toString());
				double dist = distance(pair.getFirst(), pair.getSecond(), xGoal, yGoal);
				if (dist < shortestDist && pair != path.get(path.size() - 2))
				{
					shortestPoint = pair;
					shortestDist = dist;
					indexShortest = i;
				}
				i++;
			}

			if (indexShortest == 3 || indexShortest == 2)
			{
				currentVector = normalVector;
			}
			currentPosition = shortestPoint;

			sign++;
		}

		return path;

	}

	private static double distance(float x1, float y1, float x2, float y2)
	{
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	// @Override
	// protected void paintComponent(Graphics g)
	// {
	// // TODO Stub di metodo generato automaticamente
	// super.paintComponent(g);
	// Graphics2D g2 = (Graphics2D) g;
	// System.out.println("draw");
	// // g2.draw(line);
	// // g2.draw(rect);
	// }
	//
	// public static void main(String[] args)
	// {
	// LongRangePathCalculator longRangePathCalculator = new LongRangePathCalculator();
	// JFrame frame = new JFrame();
	// frame.add(longRangePathCalculator);
	// frame.setLocation(100, 100);
	// frame.setSize(1000, 1000);
	// frame.setVisible(true);
	// }

}
