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

	private GameProperties()
	{
		loadBuildings();
		loadPaths();
		loadDimensionObjects();
	}

	public static GameProperties getInstance()
	{
		if (instance == null)
		{
			instance = new GameProperties();

		}
		return instance;

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
			if (entry.getKey().equals(key.toLowerCase()))
			{
				return (String) entry.getValue();
			}

		}
		return "";

	}

	public Integer getObjectWdth(String key)
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
}
