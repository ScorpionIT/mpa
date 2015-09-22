package mpa.core.maths;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.Pair;
import mpa.core.logic.World;
import mpa.core.util.GameProperties;

import com.jme3.math.Vector3f;

public class MyMath
{
	private static float scalarProduct(Vector2f u, Vector2f v)
	{
		return ((u.x * v.x) + (u.y * v.y));
	}

	public static double distanceInteger(Integer x1, Integer y1, Integer x2, Integer y2)
	{
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	public static float distanceFloat(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	public static float distanceFloat(Vector2f p1, Vector2f p2)
	{
		return (float) Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
	}

	public static boolean isThereAnyCollision(Integer currentX, Integer currentY, Integer xGoal, Integer yGoal, World world)
	{
		List<AbstractObject> objectsX;
		List<AbstractObject> objectsY;

		if (xGoal.floatValue() > world.getWidth() || xGoal.floatValue() < 0.0f || yGoal.floatValue() > world.getHeight() || yGoal.floatValue() < 0.0f)
			return true;

		if (currentX < xGoal)
			objectsX = world.getObjectsXInTheRange(currentX, xGoal);
		else
			objectsX = world.getObjectsXInTheRange(xGoal, currentX);

		if (currentY < yGoal)
			objectsY = world.getObjectsYInTheRange(currentY, yGoal);
		else
			objectsY = world.getObjectsYInTheRange(yGoal, currentY);

		List<AbstractObject> intersection = new ArrayList<>();

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

		for (AbstractObject abstractObject : intersection)
		{
			if ((pointToLineDistance(currentX, currentY, xGoal, yGoal, new Integer((int) abstractObject.getX()),
					new Integer((int) abstractObject.getY())) - abstractObject.getCollisionRay()) <= 0)
			{
				return true;
			}
		}

		return false;

	}

	public static double pointToLineDistance(Integer currentX, Integer currentY, Integer xGoal, Integer yGoal, Integer xObj, Integer yObj)
	{
		Pair<Integer, Integer> vector = new Pair<Integer, Integer>(-currentX + xGoal, -currentY + yGoal);
		Pair<Integer, Integer> vectorToPoint = new Pair<Integer, Integer>(-currentX + xObj, -currentY + yObj);

		double scalarProduct = (double) ((double) (vector.getFirst() * vectorToPoint.getFirst() + vector.getSecond() * vectorToPoint.getSecond()) / (double) (vector
				.getFirst() * vector.getFirst() + vector.getSecond() * vector.getSecond()));

		Pair<Double, Double> proj = new Pair<Double, Double>(vectorToPoint.getFirst() - (scalarProduct * vector.getFirst()),
				vectorToPoint.getSecond() - scalarProduct * vector.getSecond());

		return Math.sqrt(proj.getFirst() * proj.getFirst() + proj.getSecond() * proj.getSecond());
	}

	public static float pointToLineDistance(float xMinLine, float yMinLine, float xMaxLine, float yMaxLine, float xPoint, float yPoint)
	{

		Vector2f line = new Vector2f(-xMinLine + xMaxLine, -yMinLine + yMaxLine);
		Vector2f pointToVector = new Vector2f(xPoint - xMinLine, yPoint - yMinLine);
		float scalarProduct = (pointToVector.x * line.x + pointToVector.y * line.y) / (line.x * line.x + line.y * line.y);
		Vector2f projectionOnLine = new Vector2f(line.x * scalarProduct, line.y * scalarProduct);

		float distance = (float) Math.sqrt(Math.pow(pointToVector.x - projectionOnLine.x, 2) + Math.pow(pointToVector.y - projectionOnLine.y, 2));
		return distance;

	}

	public static int greatestCommonDivisor(int p, int q)
	{
		if (q < 0)
			q *= -1;

		if (p < 0)
			p *= -1;

		if (q == 0)
		{
			return p;
		}
		return greatestCommonDivisor(q, p % q);
	}

	static int calling = 0;

	public static float getRotationAngle(Vector2f from, Vector2f to)
	{
		float angle = angleBetweenVectors(from, to);
		Vector2f[] vectors = new Vector2f[2];
		vectors[0] = from;
		vectors[1] = to;

		int[] quarters = new int[2];
		float direction = 1f;

		for (int i = 0; i < 2; i++)
		{
			Vector2f tmp = vectors[i];
			if (tmp.x >= 0f && tmp.y >= 0f) // i-vector is in the first quarter
				quarters[i] = 1;
			else if (tmp.x <= 0f && tmp.y >= 0f) // i-vector is in the second quarter
				quarters[i] = 2;
			else if (tmp.x <= 0f && tmp.y <= 0f) // i-vector is in the third quarter
				quarters[i] = 3;
			else if (tmp.x >= 0f && tmp.y <= 0f) // i-vector is in the fourth quarter
				quarters[i] = 4;
		}

		if (quarters[0] == quarters[1])
		{
			float angle_y_from = angleBetweenVectors(new Vector2f(0, 1), from);
			float angle_y_to = angleBetweenVectors(new Vector2f(0, 1), to);

			switch (quarters[0])
			{
				case 1: // both in the first quarter
					if (angle_y_from < angle_y_to)
						direction = -1f;
					break;

				case 2: // both in the second quarter
					if (angle_y_from > angle_y_to)
						direction = -1f;
					break;

				case 3: // both in the third quarter
					if (angle_y_from > angle_y_to)
						direction = -1f;
					break;

				case 4: // both in the fourth quarter
					if (angle_y_from < angle_y_to)
						direction = -1f;
			}

		}
		else
		{
			if (quarters[0] > quarters[1])
				direction = -1f;
			if (quarters[0] == 4 && quarters[1] == 1)
				direction = 1f;
		}

		return angle * direction;
	}

	private static float angleBetweenVectors(Vector2f from, Vector2f to)
	{
		float cosine = scalarProduct(from, to) / (distanceFloat(0, 0, from.x, from.y) * distanceFloat(0, 0, to.x, to.y));

		return (float) Math.acos(cosine);
	}

	public static Vector3f rotateY(Vector3f vector, double angle)
	{

		Vector3f newVector = new Vector3f();
		newVector.y = vector.y;

		vector.x = (float) (vector.x * Math.cos(angle) + vector.z * Math.sin(angle));
		vector.z = (float) (-vector.x * Math.sin(angle) + vector.z * Math.cos(angle));
		return vector;

	}

	public static float scaleFactor(Vector3f modelSize, String type)
	{
		int scale = 30;
		int objectWidth = GameProperties.getInstance().getObjectWidth(type);
		int objectHeight = GameProperties.getInstance().getObjectHeight(type);
		float x = (objectWidth - objectWidth * scale / 100) / modelSize.x;
		float z = (objectHeight - objectHeight * scale / 100) / modelSize.z;
		return Math.min(x, z);

	}

	public static Vector2f computeDirection(Vector2f point1, Vector2f point2)
	{
		Vector2f direction = new Vector2f(-point1.x + point2.x, -point1.y + point2.y);

		float maxValue;

		if (Math.abs(direction.x) >= Math.abs(direction.y))
			maxValue = Math.abs(direction.x);
		else
			maxValue = Math.abs(direction.y);

		if (maxValue > 1)
			direction.set(direction.x / maxValue, direction.y / maxValue);

		return direction;
	}

	public static List<Vector2f> computeRandomPointsCircumference(Vector2f centre, float ray, int numberOfPoints)
	{

		List<Vector2f> points = new ArrayList<>();
		float alpha = (-2) * centre.x;
		float beta = (-2) * centre.y;
		double gamma = Math.pow(centre.x, 2) + Math.pow(centre.y, 2) - Math.pow(ray, 2);

		float a = 1;
		float b = beta;

		float xMin = centre.x - ray;
		float xMax = centre.x + ray;
		for (int i = 0; i < numberOfPoints / 2 + 1; i++)
		{
			float x = (float) (xMin + Math.random() * (xMax - xMin));
			double c = Math.pow(x, 2) + alpha * x + gamma;
			float y1 = (float) (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);

			points.add(new Vector2f(x, y1));

			float y2 = (float) (-b - Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
			points.add(new Vector2f(x, y2));

		}
		return points;

	}
}
