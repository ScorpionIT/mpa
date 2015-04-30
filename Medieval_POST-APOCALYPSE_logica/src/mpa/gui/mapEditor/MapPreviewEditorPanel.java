package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import javax.imageio.ImageIO;

import mpa.core.logic.Pair;
import mpa.core.util.GameProperties;
import mpa.gui.menuMap.MpaPanel;

public class MapPreviewEditorPanel extends MpaPanel
{
	private Image backgroundImage;
	private Stack<Pair<String, Point>> undoObjects = new Stack<>();
	private Stack<Pair<String, Point>> redoObjects = new Stack<>();
	private Stack<Pair<String, Point>> addedObjects = new Stack<>();
	private HashMap<String, Image> images;

	private Point selectedObjectPosition = null;
	private Color selectedObjectColor;
	private String selectedObjectName;
	private MainMapEditorPanel mainMapEditorPanel;
	private boolean market = false;
	private Pair<Integer, Integer> oldMapDimension;
	private String texturePath = GameProperties.getInstance().getPath("TexturePath");
	private int indexObject = 0;

	public MapPreviewEditorPanel(MainMapEditorPanel mainMapEditorPanel, HashMap<String, Image> imageLabels)
	{

		this.mainMapEditorPanel = mainMapEditorPanel;
		this.images = imageLabels;
		this.setLayout(null);
		try
		{

			backgroundImage = ImageIO.read(new File(texturePath + "/grass-pattern.png"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		MapPreviewListener mapPreviewListener = new MapPreviewListener(this);
		this.addMouseListener(mapPreviewListener);
		this.addMouseMotionListener(mapPreviewListener);

		this.setVisible(true);
	}

	private void setBackgroundPatternImage(Graphics g)
	{
		int backgroundWidth = backgroundImage.getWidth(this);
		int backgroundHeight = backgroundImage.getWidth(this);
		int maxI = this.getWidth() / backgroundWidth;
		int maxJ = this.getHeight() / backgroundHeight;

		for (int i = 0; i <= maxI; i++)
		{
			for (int j = 0; j <= maxJ; j++)
			{
				g.drawImage(backgroundImage, i * backgroundWidth, j * backgroundHeight, backgroundWidth, backgroundHeight, this);
			}
		}

	}

	public void addLabel(boolean moving)
	{
		if (selectedObjectPosition != null)
		{
			if (selectedObjectName.toLowerCase().equals("headquarter"))
				mainMapEditorPanel.addPlayer();
			else if (selectedObjectName.toLowerCase().equals("market"))
				market = true;
			redoObjects.removeAllElements();
			Pair<String, Point> pair = new Pair<String, Point>(selectedObjectName, selectedObjectPosition);
			addedObjects.push(pair);
			if (!moving)
				undoObjects.push(pair);
			selectedObjectColor = null;
			selectedObjectName = null;
			selectedObjectPosition = null;
			this.repaint();
		}

	}

	public boolean thereIsIntersection(Point selectedLabelBounds, String selectedObjectName)
	{

		for (int i = 0; i < addedObjects.size(); i++)
		{

			String objName = getObjectName(addedObjects.get(i));

			Rectangle rect = new Rectangle((int) (addedObjects.get(i).getSecond().getX()), (int) addedObjects.get(i).getSecond().getY(),
					(int) W((float) GameProperties.getInstance().getObjectWidth(objName)), (int) H((float) GameProperties.getInstance()
							.getObjectWidth(objName)));

			Rectangle rect1 = new Rectangle((int) selectedLabelBounds.getX(), (int) selectedLabelBounds.getY(), (int) W((float) GameProperties
					.getInstance().getObjectWidth(getObjectName(selectedObjectName))), (int) H((float) GameProperties.getInstance().getObjectHeight(
					getObjectName(selectedObjectName))));

			if (rect.intersects(rect1))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setBackgroundPatternImage(g);

		if (selectedObjectPosition != null)
		{
			g.setColor(selectedObjectColor);
			String selectedName = getObjectName(selectedObjectName);
			g.drawRect((int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(), (int) W((float) GameProperties
					.getInstance().getObjectWidth(selectedName)), (int) H((float) GameProperties.getInstance().getObjectHeight(selectedName)));

			g.drawImage(images.get(selectedName), (int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(),
					(int) W((float) GameProperties.getInstance().getObjectWidth(selectedName)), (int) H((float) GameProperties.getInstance()
							.getObjectHeight(selectedName)), this);
		}

		for (Pair<String, Point> element : addedObjects)
		{

			String[] parts = element.getFirst().split(" ");
			Image image = images.get(parts[0]);
			g.drawImage(image, (int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) GameProperties.getInstance()
					.getObjectWidth(parts[0])), (int) H((float) GameProperties.getInstance().getObjectHeight(parts[0])), this);

		}

	}

	public void undo()
	{

		if (undoObjects.size() > 0)
		{
			Pair<String, Point> removedObject = undoObjects.pop();
			Pair<String, Point> removeIfPresent = removeIfPresent(removedObject, addedObjects);
			if (removeIfPresent != null)
			{
				if (removedObject.getSecond() != removeIfPresent.getSecond())
				{
					addedObjects.add(removedObject);
					if (removedObject.getFirst().toLowerCase().equals("headquarter"))
						mainMapEditorPanel.removePlayer();
					else if (removedObject.getFirst().toLowerCase().equals("market"))
						market = false;
				}
				redoObjects.add(removeIfPresent);
			}
		}
		this.repaint();
	}

	private Pair<String, Point> removeIfPresent(Pair<String, Point> object, Stack<Pair<String, Point>> objectList)
	{
		for (int i = 0; i < objectList.size(); i++)
		{
			if (objectList.get(i).getFirst().equals(object.getFirst()))
			{
				Pair<String, Point> removed = objectList.remove(i);
				return removed;
			}

		}
		return null;

	}

	public void redo()
	{

		if (redoObjects.size() > 0)
		{
			Pair<String, Point> readdedObject = redoObjects.pop();
			Pair<String, Point> removeIfPresent = removeIfPresent(readdedObject, addedObjects);

			if (removeIfPresent != null)
			{
				addedObjects.add(readdedObject);
				undoObjects.add(removeIfPresent);
			}
			else
			{

				addedObjects.add(readdedObject);
				undoObjects.add(readdedObject);
				if (readdedObject.getFirst().toLowerCase().equals("headquarter"))
					mainMapEditorPanel.addPlayer();
				else if (readdedObject.getFirst().toLowerCase().equals("market"))
					market = true;
			}

		}
		this.repaint();

	}

	// TODO sistemare resize oggetti e vedere perchè thereisintersection va in null pointer ogni
	// tanto

	@Override
	public void setMapDimension(float width, float height)
	{
		oldMapDimension = new Pair<Integer, Integer>((int) this.getWorldHeight(), (int) this.getWorldHeight());
		// oldMapDimension.setFirst((int) getWorldHeight());
		// oldMapDimension.setSecond((int) getWorldHeight());
		super.setMapDimension(width, height);

		Rectangle map = new Rectangle(0, 0, (int) width, (int) height);
		// resize Object and remove objects outside the resized map

		resizeObject(addedObjects, map);
		resizeObject(redoObjects, map);
		resizeObject(undoObjects, map);

		// for (int i = 0; i < redoObjects.size(); i++)
		// {
		// Pair<String, Point> element = redoObjects.get(i);
		// element.getSecond().setLocation((int) graphicX((float) element.getSecond().getX()), (int)
		// graphicX((float) element.getSecond().getY()));
		//
		// Rectangle objectBounds = new Rectangle((int) element.getSecond().getX(), (int)
		// element.getSecond().getY(), (int) W((float) GameProperties
		// .getInstance().getObjectWdth(element.getFirst())), (int) H((float)
		// GameProperties.getInstance().getObjectHeight(
		// element.getFirst())));
		//
		// if (!map.contains(objectBounds))
		// {
		// redoObjects.remove(element);
		// i--;
		//
		// }
		// }
	}

	private void resizeObject(Stack<Pair<String, Point>> list, Rectangle map)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Pair<String, Point> element = list.get(i);
			String elementName = getObjectName(element);
			Point point = new Point(resizeX(element.getSecond().getX()), resizeY(element.getSecond().getY()));
			// element.getSecond().setLocation(resizeX(element.getSecond().getX()),
			// resizeY(element.getSecond().getY()));
			element.setSecond(point);
			System.out.println("vecchio punto "+ element.getSecond().getX()+ " "+element.getSecond().getY());
			// Rectangle objectBounds = new Rectangle((int) worldX((float)
			// element.getSecond().getX()),
			// (int) worldY((float) element.getSecond().getY()), (int) W((float)
			// GameProperties.getInstance().getObjectWidth(
			// getObjectName(element.getFirst()))), (int) H((float)
			// GameProperties.getInstance().getObjectHeight(
			// getObjectName(elementName))));

			// if (!map.contains(objectBounds))
			// {
			// list.remove(element);
			// i--;
			//
			// }
		}

	}

	public boolean isThereAMarket()
	{
		return market;
	}

	public void setSelectedObject(Point elementPosition, String selectedObjectName)
	{

		this.selectedObjectName = selectedObjectName + " " + indexObject;
		this.selectedObjectPosition = elementPosition;
		indexObject++;

	}

	public void paintSelectedObject(Color color)
	{
		this.selectedObjectColor = color;
		this.repaint();

	}

	public Pair<String, Point> thereIsAnObject(Point clickPosition)
	{
		for (int i = 0; i < addedObjects.size(); i++)
		{
			Rectangle objectBounds = new Rectangle((int) addedObjects.get(i).getSecond().getX(), (int) addedObjects.get(i).getSecond().getY(),
					(int) W((float) GameProperties.getInstance().getObjectWidth(getObjectName(addedObjects.get(i)))), (int) H((float) GameProperties
							.getInstance().getObjectHeight(getObjectName(addedObjects.get(i)))));

			if (objectBounds.contains(clickPosition))
			{
				if (getObjectName(addedObjects.get(i)).toLowerCase().equals("market"))
					market = false;
				else if (getObjectName(addedObjects.get(i)).toLowerCase().equals("headquarter"))
					mainMapEditorPanel.removePlayer();
				Pair<String, Point> removed = addedObjects.remove(i);
				undoObjects.add(removed);
				return removed;
			}

		}
		return null;
	}

	public Stack<Pair<String, Point>> getAddedObjects()
	{

		for (Pair<String, Point> addedObject : addedObjects)
		{
			addedObject.getSecond().setLocation(worldX((float) addedObject.getSecond().getX()), worldY((float) addedObject.getSecond().getY()));
		}
		return addedObjects;
	}

	public void setMovingObject(String movingObjectName, Point movingObjectPosition)
	{
		this.selectedObjectName = movingObjectName;
		this.selectedObjectPosition = movingObjectPosition;

	}

	public void resetMovingObject()
	{
		Pair<String, Point> readdedObject = undoObjects.pop();
		if (getObjectName(readdedObject).toLowerCase().equals("headquarter"))
			mainMapEditorPanel.addPlayer();
		else if (getObjectName(readdedObject).toLowerCase().equals("market"))
			market = true;
		addedObjects.push(readdedObject);

		selectedObjectColor = null;
		selectedObjectName = null;
		selectedObjectPosition = null;
		this.repaint();

	}

	private int resizeX(double oldX)
	{

		int oldWorldPosition = (int) ((oldX * oldMapDimension.getFirst()) / this.getWidth());

		int newWorldPosition = (int) ((oldWorldPosition * oldMapDimension.getFirst()) / this.getWorldWidth());

		return graphicX(newWorldPosition);

	}

	private int resizeY(double oldY)
	{

		int oldWorldPosition = (int) ((oldY * oldMapDimension.getSecond()) / this.getHeight());

		int newWorldPosition = (int) ((oldWorldPosition * oldMapDimension.getSecond()) / this.getWorldHeight());

		return graphicY(newWorldPosition);
	}

	private String getObjectName(Pair<String, Point> object)
	{
		return getObjectName(object.getFirst());
	}

	private String getObjectName(String object)
	{
		String[] parts = object.split(" ");
		return parts[0];
	}
}
