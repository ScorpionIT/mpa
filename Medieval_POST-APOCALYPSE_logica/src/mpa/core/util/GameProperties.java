package mpa.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

public class GameProperties
{
	private static GameProperties instance = null;
	private Map<String, Integer> objectWidth = new HashMap<>();
	private Map<String, Integer> objectHeght = new HashMap<>();
	private Map<String, String> paths = new HashMap<String, String>();
	private List<String> worldObjects = new ArrayList<>();
	private Map<String, Map<String, Integer>> prices = new HashMap<>();
	private Map<String, Float> collisionRays = new HashMap<>();
	private Map<String, Float> rotationAngle = new HashMap<>();

	private String[] characterType = { "player", "minion", "towerCrusher" };
	private Map<String, String> modelsName = new HashMap<>();

	private GameProperties()
	{
		loadBuildings();
		loadPaths();
		loadDimensionObjects();
		loadPrices();
		loadRotationAngle();
		loadModelsName();

		for (String obj : objectHeght.keySet())
		{
			float xMin = objectWidth.get(obj) / 2;
			float yMin = objectHeght.get(obj) / 2;
			float collisionRay = (float) Math.sqrt(Math.pow(xMin, 2) + Math.pow(yMin, 2));
			collisionRays.put(obj.toLowerCase(), collisionRay);
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

			Map<String, Integer> _prices = new HashMap<>();
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

		for (String character : characterType)
		{

			String width = properties.getProperty(character + "Width");
			String height = properties.getProperty(character + "Height");
			if (width != null && height != null)
			{

				objectWidth.put(character.toLowerCase(), Integer.parseInt(width));
				objectHeght.put(character.toLowerCase(), Integer.parseInt(height));
			}
		}
	}

	private void loadModelsName()
	{

		Properties properties = null;
		FileInputStream fileInput = null;
		try
		{
			fileInput = new FileInputStream(new File("./Properties/ModelsName.properties"));
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
			String name = properties.getProperty(object);
			if (name != null)
			{
				modelsName.put(object.toLowerCase(), name);
			}
		}

		for (String character : characterType)
		{
			String name = properties.getProperty(character);
			if (name != null)
			{
				modelsName.put(character.toLowerCase(), name);
			}
		}
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
			rotationAngle.put((String) object, Float.parseFloat(properties.getProperty((String) object)));
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
			JOptionPane.showMessageDialog(null, "Properties File cannot be found or it is corrupted :(", "UNRECOVERABLE ERROR",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} finally
		{

			boolean closed = true;
			do
			{
				try
				{
					fileInput.close();
				} catch (IOException e)
				{
					closed = false;
				}
			} while (!closed);
		}

		for (Object key : properties.keySet())
		{
			paths.put(((String) key).toLowerCase(), properties.getProperty((String) key));

		}

	}

	public String getPath(String key)
	{
		return paths.get(key.toLowerCase());

	}

	public String getModelName(String key)
	{
		return modelsName.get(key.toLowerCase());

	}

	public Integer getObjectWidth(String key)
	{
		return objectWidth.get(key.toLowerCase());

	}

	public Integer getObjectHeight(String key)
	{
		return objectHeght.get(key.toLowerCase());

	}

	public List<String> getWorldObject()
	{
		return worldObjects;
	}

	public Map<String, Integer> getPrices(String type)
	{
		return prices.get(type);
	}

	public float getCollisionRay(String type)
	{

		return collisionRays.get(type.toLowerCase());
	}

	public float getRotationAngle(String object)
	{

		String[] split = object.split("-");
		for (String modelName : rotationAngle.keySet())
		{
			if (modelName.equals(split[0]))
			{
				return rotationAngle.get(modelName);
			}

		}
		return 0f;
	}
}
