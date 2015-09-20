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
	private Pair<Integer, Integer> oldMapDimension;
	private String texturePath = GameProperties.getInstance().getPath("TexturePath");
	private int indexObject = 0;
	private Rectangle panelRectangle;

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

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);

		panelRectangle = new Rectangle(0, 0, width, height);
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
			if (getObjectName(selectedObjectName).toLowerCase().equals("headquarter"))
				mainMapEditorPanel.addPlayer();

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

		Rectangle selectedObjectRect = new Rectangle((int) selectedLabelBounds.getX(), (int) selectedLabelBounds.getY(),
				(int) W((float) GameProperties.getInstance().getObjectWidth(getObjectName(selectedObjectName)) * 2), (int) H((float) GameProperties
						.getInstance().getObjectHeight(getObjectName(selectedObjectName)) * 2));
		for (int i = 0; i < addedObjects.size(); i++)
		{

			String objName = getObjectName(addedObjects.get(i));

			Rectangle rect = new Rectangle((int) (addedObjects.get(i).getSecond().getX()), (int) addedObjects.get(i).getSecond().getY(),
					(int) W((float) GameProperties.getInstance().getObjectWidth(objName) * 2), (int) H((float) GameProperties.getInstance()
							.getObjectWidth(objName) * 2));

			if (rect.intersects(selectedObjectRect))
			{
				return true;
			}
		}
		if (!panelRectangle.contains(selectedObjectRect))
		{
			return true;
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

			Integer objectWidth = GameProperties.getInstance().getObjectWidth(selectedName);
			Integer objectHeight = GameProperties.getInstance().getObjectHeight(selectedName);

			g.drawRect((int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(), W(objectWidth * 2), H(objectHeight * 2));

			g.drawImage(images.get(selectedName), (int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(),
					W(objectWidth * 2), H(objectHeight * 2), this);

		}

		for (Pair<String, Point> element : addedObjects)
		{

			String elementName = getObjectName(element.getFirst());
			Image image = images.get(elementName);
			Integer objectWidth = GameProperties.getInstance().getObjectWidth(elementName);
			Integer objectHeight = GameProperties.getInstance().getObjectHeight(elementName);

			g.drawImage(image, (int) (element.getSecond().getX()), (int) element.getSecond().getY(), W(objectWidth * 2), H(objectHeight * 2), this);

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
					if (getObjectName(removedObject.getFirst()).toLowerCase().equals("headquarter"))
						mainMapEditorPanel.removePlayer();

				}
				redoObjects.add(removeIfPresent);
			}

			else
			{
				addedObjects.add(removedObject);
				if (getObjectName(removedObject.getFirst()).toLowerCase().equals("headquarter"))
					mainMapEditorPanel.addPlayer();
				redoObjects.add(removedObject);

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
				if (getObjectName(readdedObject).toLowerCase().equals("headquarter"))
					mainMapEditorPanel.addPlayer();

			}

		}
		this.repaint();

	}

	@Override
	public void setMapDimension(float width, float height)
	{
		oldMapDimension = new Pair<Integer, Integer>((int) this.getWorldWidth(), (int) this.getWorldHeight());
		super.setMapDimension(width, height);
		Rectangle map = new Rectangle(0, 0, (int) width, (int) height);

		resizeObject(addedObjects, map);
		resizeObject(redoObjects, map);
		resizeObject(undoObjects, map);

	}

	private void resizeObject(Stack<Pair<String, Point>> list, Rectangle map)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Pair<String, Point> element = list.get(i);
			String elementName = getObjectName(element);

			Point point = new Point(resizeX(element.getSecond().getX()), resizeY(element.getSecond().getY()));
			element.setSecond(point);

			Integer objectWidth = GameProperties.getInstance().getObjectWidth(elementName);
			Integer objectHeight = GameProperties.getInstance().getObjectHeight(elementName);

			Rectangle objectBounds = new Rectangle(worldX((float) element.getSecond().getX()), worldY((float) element.getSecond().getY()),
					objectWidth, objectHeight);

			if (!map.contains(objectBounds))
			{
				list.remove(element);
				i--;

			}
		}

	}

	public void setSelectedObject(Point elementPosition, String selectedObjectName)
	{
		if (selectedObjectName == null)
		{
			this.selectedObjectName = null;
			this.selectedObjectPosition = null;
			return;
		}

		Integer objectWeight = GameProperties.getInstance().getObjectWidth(getObjectName(selectedObjectName));
		Integer objectHeight = GameProperties.getInstance().getObjectHeight(getObjectName(selectedObjectName));

		this.selectedObjectName = selectedObjectName + " " + indexObject;
		elementPosition.setLocation(elementPosition.getX() - objectWeight / 2, elementPosition.getY() - objectHeight / 2);
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
					(int) W((float) GameProperties.getInstance().getObjectWidth(getObjectName(addedObjects.get(i))) * 2),
					(int) H((float) GameProperties.getInstance().getObjectHeight(getObjectName(addedObjects.get(i))) * 2));

			if (objectBounds.contains(clickPosition))
			{

				if (getObjectName(addedObjects.get(i)).toLowerCase().equals("headquarter"))
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
			addedObject.setFirst(getObjectName(addedObject.getFirst()));
			addedObject.getSecond().setLocation(
					worldX((float) (addedObject.getSecond().getX() + GameProperties.getInstance().getObjectWidth(getObjectName(addedObject)))),
					worldY((float) addedObject.getSecond().getY() + GameProperties.getInstance().getObjectHeight(getObjectName(addedObject))));
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
		if (undoObjects.size() > 0)
		{
			Pair<String, Point> readdedObject = undoObjects.pop();
			if (getObjectName(readdedObject).toLowerCase().equals("headquarter"))
				mainMapEditorPanel.addPlayer();

			addedObjects.push(readdedObject);

			selectedObjectColor = null;
			selectedObjectName = null;
			selectedObjectPosition = null;
			this.repaint();
		}

	}

	private int resizeX(double oldX)
	{
		if (oldMapDimension.getFirst() == this.getWorldWidth())
			return (int) oldX;

		double oldWorldPosition = ((oldX * oldMapDimension.getFirst()) / this.getWidth());

		return graphicX((float) oldWorldPosition);

	}

	private int resizeY(double oldY)
	{
		if (oldMapDimension.getSecond() == this.getWorldHeight())
			return (int) oldY;

		double oldWorldPosition = (oldY * oldMapDimension.getSecond()) / this.getHeight();

		return graphicY((float) oldWorldPosition);
	}

	private String getObjectName(Pair<String, Point> object)
	{
		return getObjectName(object.getFirst());
	}

	public String getObjectName(String object)
	{
		String[] parts = object.split(" ");
		return parts[0];
	}

	public boolean getDeleteMode()
	{
		return mainMapEditorPanel.getDeleteMode();
	}
}
