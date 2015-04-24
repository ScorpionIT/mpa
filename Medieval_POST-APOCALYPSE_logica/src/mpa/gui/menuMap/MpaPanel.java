package mpa.gui.menuMap;

import javax.swing.JPanel;

public class MpaPanel extends JPanel
{

	int worldWidth;
	int worldHeight;

	public MpaPanel()
	{
		super();
	}

	public int graphicX(float x)
	{

		int newPosition = (int) ((x * this.getWidth()) / worldWidth);
		return newPosition;
	}

	public int graphicY(float y)
	{
		int newPosition = (int) ((y * this.getHeight()) / worldHeight);
		return newPosition;
	}

	public int worldX(float x)
	{

		int newPosition = (int) ((x * worldWidth) / this.getWidth());
		return newPosition;
	}

	public int worldY(float y)
	{
		int newPosition = (int) ((y * worldHeight) / this.getHeight());
		return newPosition;
	}

	public int W(float w)
	{
		int x = (int) (w * this.getWidth() / worldWidth);
		return (int) x;
	}

	public int H(float h)
	{
		int x = (int) (h * this.getHeight() / worldHeight);
		return (int) x;
	}

	public void setMapDimension(float width, float height)
	{
		this.worldWidth = (int) width;
		this.worldHeight = (int) height;
	}
}
