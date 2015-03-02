package mpa.gui.menuMap;

import javax.swing.JPanel;

public class MpaPanel extends JPanel
{

	public MpaPanel()
	{
		super();
	}

	// TODO tutta la classe

	public int X(float x)
	{
		return (int) x;
	}

	public int Y(float y)
	{
		return (int) y;
	}

	public int W(float w)
	{

		System.out.println(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		this.getWidth();
		return (int) w;
	}

	public int H(float h)
	{

		this.getHeight();
		return (int) h;
	}
}
