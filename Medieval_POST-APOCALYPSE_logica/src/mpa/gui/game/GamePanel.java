package mpa.gui.game;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import mpa.core.logic.MapManager;
import mpa.core.logic.World;

@SuppressWarnings("serial")
public class GamePanel extends JPanel
{
	private int lineGap;
	private int lines;
	private int playerX;
	private int playerY;
	private ArrayList<Point> path;

	private static enum Move
	{
		LEFT, RIGHT, UP, DOWN
	};

	private MapManager mapManager;
	private World world;

	public GamePanel()
	{
		this.playerX = 2;
		this.playerY = 2;
		this.path = new ArrayList<Point>();
		mapManager = new MapManager("/home/parcuri/Medieval Post APOCALYPSE/MappeGioco/ciao2.txt");
		try
		{
			world = mapManager.decode();
		} catch (Exception e)
		{
			// TODO Blocco catch generato automaticamente
			System.out.println("sono morto");
			e.printStackTrace();
		}
		// this.lines = this.world.getHeight();
		// this.Obstacles = new ArrayList<Point>();
		// this.Obstacles.add (new Point (4, 3));
		// this.Obstacles.add (new Point (6, 6));
		// this.Obstacles.add (new Point (5, 5));
	}

	// @Override
	// protected void paintComponent(Graphics g)
	// {
	// super.paintComponent(g);
	// int width = getWidth();
	// int height = getHeight();
	//
	// lineGap = getWidth() / lines;
	//
	// int x = lineGap;
	// int y = 0;
	//
	// for (int i = 0; i < lines; i++)
	// {
	// g.drawLine(x, 0, x, height);
	// x += lineGap;
	// }
	//
	// x = 0;
	// y = 0;
	//
	// for (int i = 0; i < this.world.getWidth(); i++)
	// {
	// g.drawLine(0, y, width, y);
	// y += lineGap;
	// }
	//
	// g.setColor(Color.gray);
	// g.drawRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);
	// g.setColor(Color.cyan);
	// g.fillRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);
	//
	// g.setColor(Color.gray);
	//
	// for (int i = 0; i < this.world.getWidth(); i++)
	// {
	// for (int j = 0; j < this.world.getWidth(); j++)
	// {
	// if (!(this.world.getMap()[i][j] instanceof Object))
	// {
	// g.setColor(Color.gray);
	// g.drawRect(i * this.lineGap + 1, j * this.lineGap + 1, lineGap - 2, lineGap - 2);
	// g.setColor(Color.red);
	// g.fillRect(i * this.lineGap + 1, j * this.lineGap + 1, lineGap - 2, lineGap - 2);
	// }
	// }
	// }
	// for (final Point point : this.path)
	// {
	// g.setColor(Color.gray);
	// g.drawRect(point.x * this.lineGap + 1, point.y * this.lineGap + 1, lineGap - 2, lineGap - 2);
	// g.setColor(Color.green);
	// g.fillRect(point.x * this.lineGap + 1, point.y * this.lineGap + 1, lineGap - 2, lineGap - 2);
	// }
	// }
	//
	// public void setPlayerPosition(int clickX, int clickY)
	// {
	// // this.playerX = clickX / this.lineGap;
	// // this.playerY = clickY / this.lineGap;
	//
	// this.path.clear();
	//
	// if (computePath(this.playerX, this.playerY, clickX / this.lineGap, clickY / this.lineGap,
	// this.path))
	// {
	// System.out.println("il path è ");
	//
	// for (final Point point : this.path)
	// {
	// System.out.print(" x : " + point.x + " ");
	// System.out.println(" y : " + point.y);
	//
	// }
	// }
	// else
	// System.out.println("Destinazione non raggiungibile");
	//
	// }
	//
	// public boolean computePath(int initX, int initY, int destX, int destY, ArrayList<Point> path)
	// {
	// if (initX == destX && initY == destY)
	// return true;
	//
	// if (initX > world.getHeight() || initY > world.getWidth())
	// return false;
	//
	// if (initX < 0 || initY < 0)
	// return false;
	//
	// if (!world.isPositionEmpty(destX, destY))
	// return false;
	// if (!world.isPositionEmpty(initX, initY))
	// return false;
	//
	// int currentX;
	// int currentY;
	//
	// if (!path.isEmpty())
	// {
	// currentX = path.get(path.size() - 1).x;
	// currentY = path.get(path.size() - 1).y;
	// }
	// else
	// {
	// currentX = initX;
	// currentY = initY;
	// }
	// ArrayList<Move> borders = new ArrayList<Move>();
	//
	// if (currentX < destX)
	// borders.add(Move.RIGHT);
	// else if (currentX > destX)
	// borders.add(Move.LEFT);
	//
	// if (currentY < destY)
	// borders.add(Move.DOWN);
	// else if (currentY > destY)
	// borders.add(Move.UP);
	//
	// int randomNum = 0 + (int) (Math.random() * 2);
	// int start = 0;
	// int end = borders.size();
	// int op = 1;
	//
	// if (randomNum == 1)
	// {
	// start = borders.size() - 1;
	// end = -1;
	// op = -1;
	// }
	//
	// for (int i = start; i != end; i = i + op)
	// {
	// Point point = new Point();
	// if (borders.get(i) == Move.RIGHT)
	// {
	// System.out.println("RIGHT");
	// point = new Point(currentX + 1, currentY);
	// }
	// else if (borders.get(i) == Move.LEFT)
	// {
	// System.out.println("LEFT");
	// point = new Point(currentX - 1, currentY);
	// }
	// else if (borders.get(i) == Move.UP)
	// {
	// System.out.println("UP");
	// point = new Point(currentX, currentY - 1);
	// }
	// else if (borders.get(i) == Move.DOWN)
	// {
	// System.out.println("DOWN");
	// point = new Point(currentX, currentY + 1);
	// }
	//
	// path.add(point);
	// if (this.computePath(point.x, point.y, destX, destY, path))
	// return true;
	//
	// else
	// path.remove(path.size() - 1);
	// }
	//
	// return false;
	// }
}
