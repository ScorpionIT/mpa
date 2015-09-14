package mpa.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class GameProperties
{
	private static GameProperties instance = null;
	private HashMap<String, Integer> objectWidth = new HashMap<>();
	private HashMap<String, Integer> objectHeght = new HashMap<>();
	private Set<Entry<Object, Object>> paths;
	private ArrayList<String> worldObjects = new ArrayList<>();
	private HashMap<String, HashMap<String, Integer>> prices = new HashMap<>();
	private HashMap<String, Float> collisionRays = new HashMap<>();
	private HashMap<String, Integer> rotationAngle = new HashMap<>();

	private GameProperties()
	{
		loadBuildings();
		loadPaths();
		loadDimensionObjects();
		loadPrices();
		loadRotationAngle();

		for (String obj : objectHeght.keySet())
		{
			float xMin = objectWidth.get(obj) / 2;
			float yMin = objectHeght.get(obj) / 2;
			float collisionRay = (float) Math.sqrt(Math.pow(xMin, 2) + Math.pow(yMin, 2));
			collisionRays.put(obj, collisionRay);
		}
	}

	public static GameProperties getInstance()
	{
		if (instance == null)
		{
			instance = new GameProperties();

		}
		return instance;

	}

	private void loadPrices()
	{

		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/Prices.properties"));
			properties = new Properties();
			properties.load(fileInput);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				fileInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		Set<Object> keySet = properties.keySet();
		for (Object object : keySet)
		{
			String value = properties.getProperty((String) object);
			String[] values = value.split(",");

			HashMap<String, Integer> _prices = new HashMap<>();
			int type = 0;
			for (String price : values)
			{
				switch (type)
				{
					case 0:
						_prices.put("WHEAT", Integer.parseInt(price));
						break;
					case 1:
						_prices.put("WOOD", Integer.parseInt(price));
						break;
					case 2:
						_prices.put("IRON", Integer.parseInt(price));
						break;
					case 3:
						_prices.put("STONE", Integer.parseInt(price));
						break;
					case 4:
						_prices.put("HERBS", Integer.parseInt(price));
						break;

				}
				type++;
			}
			prices.put((String) object, _prices);
		}
	}

	private void loadBuildings()
	{
		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/Buildings.properties"));
			properties = new Properties();
			properties.load(fileInput);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				fileInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		Set<Object> keySet = properties.keySet();
		for (Object object : keySet)
		{
			worldObjects.add(properties.getProperty((String) object));
		}

	}

	private void loadDimensionObjects()
	{

		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/Dimension.properties"));
			properties = new Properties();
			properties.load(fileInput);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				fileInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		for (String object : worldObjects)
		{

			String width = properties.getProperty(object + "Width");
			String height = properties.getProperty(object + "Height");
			if (width != null && height != null)
			{

				objectWidth.put(object, Integer.parseInt(width));
				objectHeght.put(object, Integer.parseInt(height));
			}
		}
		String width = properties.getProperty("playerWidth");
		String height = properties.getProperty("playerHeight");

		objectWidth.put("player", Integer.parseInt(width));
		objectHeght.put("player", Integer.parseInt(height));

	}

	private void loadRotationAngle()
	{

		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/Rotation.properties"));
			properties = new Properties();
			properties.load(fileInput);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				fileInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Set<Object> keySet = properties.keySet();
		for (Object object : keySet)
		{
			rotationAngle.put((String) object, Integer.parseInt(properties.getProperty((String) object)));
		}

	}

	private void loadPaths()
	{
		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/Paths.properties"));
			properties = new Properties();
			properties.load(fileInput);

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				fileInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		paths = properties.entrySet();

	}

	public String getPath(String key)
	{
		for (Entry<Object, Object> entry : paths)
		{
			if (entry.getKey().equals(key))
			{
				return (String) entry.getValue();
			}

		}
		return "";

	}

	public Integer getObjectWidth(String key)
	{
		return objectWidth.get(key.toLowerCase());

	}

	public Integer getObjectHeight(String key)
	{
		return objectHeght.get(key.toLowerCase());

	}

	public ArrayList<String> getWorldObject()
	{
		return worldObjects;
	}

	public HashMap<String, Integer> getPrices(String type)
	{
		return prices.get(type);
	}

	public float getCollisionRay(String type)
	{
		return collisionRays.get(type);
	}

	public int getRotationAngle(String object)
	{

		for (String modelName : rotationAngle.keySet())
		{
			if (object.contains(modelName))
			{
				return rotationAngle.get(modelName);
			}

		}
		return 0;
	}
}
