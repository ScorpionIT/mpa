package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import mpa.core.logic.Pair;

class MapPreviewListener extends MouseInputAdapter
{
	boolean dragging;
	private MapPreviewEditorPanel mapPreviewEditorPanel;
	private String movingObjectName = null;
	private Point movingObjectPosition = null;
	private int deltaX;
	private int deltaY;

	public MapPreviewListener(MapPreviewEditorPanel mapPreviewEditorPanel)
	{
		this.mapPreviewEditorPanel = mapPreviewEditorPanel;
		dragging = false;

	}

	public void mousePressed(MouseEvent e)
	{

		Pair<String, Point> movingObject = mapPreviewEditorPanel.thereIsAnObject(e.getPoint());
		if (movingObject != null)
		{
			movingObjectPosition = new Point((int) movingObject.getSecond().getX(), (int) movingObject.getSecond().getY());
			movingObjectName = movingObject.getFirst();
			deltaX = e.getPoint().x - movingObjectPosition.x;
			deltaY = e.getPoint().y - movingObjectPosition.y;
			mapPreviewEditorPanel.setMovingObject(movingObjectName, movingObjectPosition);
			dragging = true;
		}

	}

	public void mouseReleased(MouseEvent e)
	{

		if (dragging && !mapPreviewEditorPanel.thereIsIntersection(movingObjectPosition, movingObjectName))
			mapPreviewEditorPanel.addLabel(true);
		else
			mapPreviewEditorPanel.resetMovingObject();

		dragging = false;

	}

	public void mouseDragged(MouseEvent e)
	{

		if (dragging)
		{
			movingObjectPosition.x = e.getX() - deltaX;
			movingObjectPosition.y = e.getY() - deltaY;

			if (movingObjectPosition.x > 0 && movingObjectPosition.y > 0)
			{

				if (mapPreviewEditorPanel.thereIsIntersection(movingObjectPosition, movingObjectName))
					mapPreviewEditorPanel.paintSelectedObject(Color.red);
				else
					mapPreviewEditorPanel.paintSelectedObject(Color.green);
			}

		}
	}
}
