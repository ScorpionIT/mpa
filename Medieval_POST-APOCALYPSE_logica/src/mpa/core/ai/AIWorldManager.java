package mpa.core.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractProperty;
import mpa.core.logic.characters.Player;
import mpa.core.maths.MyMath;

public class AIWorldManager
{

	List<Vector2f> openList = new ArrayList<>();
	List<Vector2f> closedList = new ArrayList<>();
	List<Vector2f> allPoints = new ArrayList<>();
	float ray;
	private int fragmentsX;
	private int fragmentsY;
	private Map<Vector2f, List<AbstractObject>> buildings = new HashMap<>();

	public AIWorldManager(DifficultyLevel level)
	{
		switch (level)
		{
			case ARE_YOU_KIDDING_ME:

				break;
			case EASY:
				ray = 30f;
				break;
			case EXTREME:
				ray = 200f;
				break;
			case HARD:
				ray = 100f;
				break;
			case MEDIUM:
				ray = 50f;
				break;
			default:
				break;
		}

		fragmentsX = (int) (GameManager.getInstance().getWorld().getWidth() / ray);
		fragmentsY = (int) (GameManager.getInstance().getWorld().getHeight() / ray);
		computeOpenList();

	}

	static int counter1 = 1;
	static int counter2 = 1;

	private void computeOpenList()
	{
		for (int i = 0; i < fragmentsX; i++)
			for (int j = 0; j < fragmentsY; j++)
			{
				Vector2f position = new Vector2f(ray + ray * i, ray + ray * j);

				List<AbstractObject> collisions = GameManager.getInstance().getWorld().checkForCollision(position.x, position.y, ray);

				List<AbstractObject> buildingList = GameManager.getInstance().getWorld()
						.getObjectsInTheRange(position.x - ray, position.x + ray, position.y - ray, position.y + ray);
				if (collisions.isEmpty())
				{
					if (position.x < 0 || position.x > GameManager.getInstance().getWorld().getWidth() || position.y < 0
							|| position.y > GameManager.getInstance().getWorld().getHeight())
						System.out.println("visiterò per la " + counter1++ + " volta un posto fuori dalla mappa");
				}
				else
				{
					position = ((AbstractProperty) collisions.get(0)).getGatheringPlace();
					if (position.x < 0 || position.x > GameManager.getInstance().getWorld().getWidth() || position.y < 0
							|| position.y > GameManager.getInstance().getWorld().getHeight())
						System.out.println("visiterò per la " + counter1++ + " volta un posto fuori dalla mappa");
					System.out.println("visiterò per la " + counter2++ + " volta un posto fuori dalla mappa che è un gatheringPlace");

				}
				buildings.put(position, buildingList);
				openList.add(position);
				allPoints.add(position);
			}
	}

	public List<Vector2f> getAllPoints()
	{
		return allPoints;
	}

	Vector2f giveMeAnotherLocation(Vector2f oldLocation, Player player)
	{
		Vector2f playerPosition = new Vector2f(player.getX(), player.getY());
		float minDistance = Float.MAX_VALUE;
		Vector2f nextLocation = null;

		for (Vector2f node : openList)
		{
			float newDistance = MyMath.distanceFloat(playerPosition.x, playerPosition.y, node.x, node.y);
			if (!closedList.contains(node) && newDistance < minDistance)
			{
				minDistance = newDistance;
				nextLocation = node;
			}

		}

		if (nextLocation != null)
		{
			openList.remove(nextLocation);
			closedList.add(nextLocation);
		}

		closedList.remove(oldLocation);
		openList.add(oldLocation);

		return nextLocation;
	}

	Vector2f getNextLocation(Player player)
	{
		Vector2f playerPosition = new Vector2f(player.getX(), player.getY());
		float minDistance = Float.MAX_VALUE;
		Vector2f nextLocation = null;

		for (Vector2f node : openList)
		{
			float newDistance = MyMath.distanceFloat(playerPosition.x, playerPosition.y, node.x, node.y);
			if (!closedList.contains(node) && newDistance < minDistance)
			{
				minDistance = newDistance;
				nextLocation = node;
			}

		}

		if (nextLocation != null)
		{
			openList.remove(nextLocation);
			closedList.add(nextLocation);
		}

		return nextLocation;
	}

	List<AbstractObject> getBuildings(Vector2f position)
	{
		return buildings.get(position);
	}
}
