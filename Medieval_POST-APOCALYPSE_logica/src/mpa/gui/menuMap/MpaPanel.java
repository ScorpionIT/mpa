package mpa.gui.menuMap;

import javax.swing.JPanel;

public class MpaPanel extends JPanel
{

	int width;
	int height;

	public MpaPanel()
	{
		super();
	}

	// TODO tutta la classe

	public int X(float x)
	{

		int newPosition = (int) ((x * this.getWidth()) / width);
		// System.out.println("new position " + newPosition);
		// System.out.println("pannello " + this.getWidth() + " " + this.getHeight());
		return newPosition;
	}

	public int Y(float y)
	{
		int newPosition = (int) ((y * this.getHeight()) / height);
		return newPosition;
	}

	public int W(float w)
	{

		this.getWidth();
		return (int) w;
	}

	public int H(float h)
	{

		this.getHeight();
		return (int) h;
	}

	public void setMapDimension(float width, float height)
	{
		this.width = (int) width;
		this.height = (int) height;
	}
}
