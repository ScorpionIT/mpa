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
		int x = (int) (w * this.width / width);
		return (int) x;
	}

	public int H(float h)
	{
		int x = (int) (h * this.getHeight() / height);
		return (int) x;
	}

	public void setMapDimension(float width, float height)
	{
		this.width = (int) width;
		this.height = (int) height;
	}
}
