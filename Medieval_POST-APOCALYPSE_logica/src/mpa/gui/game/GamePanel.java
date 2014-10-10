package mpa.gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel
{
	private int lineGap;
	private final int lines = 10;
	private int playerX;
	private int playerY;
	private ArrayList<Point> path;
	private static enum Move { LEFT, RIGHT, UP, DOWN };
	
	private ArrayList<Point> Obstacles; 
	
	public GamePanel()
	{
		this.playerX = 2;
		this.playerY = 2;
		this.path = new ArrayList<Point>();
		this.Obstacles = new ArrayList<Point>();
		this.Obstacles.add (new Point (4, 3));
		this.Obstacles.add (new Point (6, 6));
		this.Obstacles.add (new Point (5, 5));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int width = getWidth();
        int height = getHeight();
        
		lineGap = getWidth() / this.lines;
		        
        int x = lineGap;
        int y = 0;
        
        for (int i = 0; i < lines; i++)
        {
            g.drawLine(x, 0, x, height);
            x += lineGap;
        }
        
        x = 0;
        y = 0;
        
        for (int i = 0; i < lines; i++)
        {
            g.drawLine(0, y, width, y);
            y += lineGap;
        }
        
        g.setColor( Color.gray );
        g.drawRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);
        g.setColor( Color.cyan );
        g.fillRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);
        
        g.setColor( Color.gray );
        
        
        for( final Point point : this.Obstacles )
        {
        	 g.setColor( Color.gray );
	    	 g.drawRect( point.x * this.lineGap +1, point.y * this.lineGap +1, lineGap -2, lineGap -2 );
	    	 g.setColor( Color.red );
	    	 g.fillRect(point.x * this.lineGap +1, point.y * this.lineGap +1, lineGap -2, lineGap -2 );
        }
        for( final Point point : this.path )
        {
        	 g.setColor( Color.gray );
	    	 g.drawRect( point.x * this.lineGap +1, point.y * this.lineGap +1, lineGap -2, lineGap -2 );
	    	 g.setColor( Color.green );
	    	 g.fillRect(point.x * this.lineGap +1, point.y * this.lineGap +1, lineGap -2, lineGap -2 );
        }
	}
	
	public void setPlayerPosition( int clickX, int clickY )
	{
//		this.playerX = clickX / this.lineGap;
//		this.playerY = clickY / this.lineGap;
		
		this.path.clear();
		
		computePath( this.playerX, this.playerY, clickX / this.lineGap, clickY / this.lineGap, this.path);
		
		System.out.println( "il path è ");
		
		for( final Point point : this.path )
		{
			System.out.print( " x : " + point.x + " " );
			System.out.println( " y : " + point.y );
			
		}
	}

	public boolean computePath( int initX, int initY, int destX, int destY, ArrayList<Point> path)
	{
		if( initX  == destX && initY == destY  )
			return true;
		
		if( initX > 10 || initY > 10 )
			return false;
		
		if( initX < 0 || initY < 0 )
			return false;

		for( final Point obstacle : this.Obstacles )
        {
			if( initX  == obstacle.x && initY == obstacle.y  )
				return false;
        }
		
		int currentX;
		int currentY;
		
		if(  !path.isEmpty() )
		{
			currentX = path.get( path.size() - 1 ).x;
			currentY = path.get( path.size() - 1 ).y;
		}
		else
		{
			currentX = initX;
			currentY = initY;
		}
		ArrayList<Move> borders = new ArrayList<Move>();
		
		int randomNum = 0 + (int)(Math.random()*2); 
		
		if (randomNum == 0)
		{
			if( currentX < destX )
				borders.add( Move.RIGHT );
			else if ( currentX > destX )
				borders.add( Move.LEFT );
			
			if( currentY < destY )
				borders.add( Move.DOWN );
			else if ( currentY > destY )
				borders.add( Move.UP );
		}
		else
		{
			if( currentY < destY )
				borders.add( Move.DOWN );
			else if ( currentY > destY )
				borders.add( Move.UP );
			
			if( currentX < destX )
				borders.add( Move.RIGHT );
			else if ( currentX > destX )
				borders.add( Move.LEFT );
		}
		
		//borders.add( new Point( currentX + 1, currentY ) );
		//borders.add( new Point( currentX + 1, currentY + 1 ) );
		//borders.add( new Point( currentX , currentY + 1 ) );
		//borders.add( new Point( currentX - 1, currentY + 1 ) );
		//borders.add( new Point( currentX - 1, currentY ) );
		//borders.add( new Point( currentX - 1, currentY - 1 ) );
		//borders.add( new Point( currentX, currentY -1 ) );
		//borders.add( new Point( currentX + 1, currentY - 1 ) );
				
		
		for( final Move direction : borders )
		{
			Point point = new Point();
			if (direction == Move.RIGHT)
			{
				System.out.println("RIGHT");
				point = new Point (currentX+1, currentY);
			}
			else if (direction == Move.LEFT)
			{
				System.out.println("LEFT");
				point = new Point (currentX-1, currentY);
			}
			else if (direction == Move.UP)
			{
				System.out.println("UP");
				point = new Point (currentX, currentY-1);
			}
			else if (direction == Move.DOWN)
			{
				System.out.println("DOWN");
				point = new Point (currentX, currentY+1);
			}
			
			path.add( point );
//			for( final Point point2 : this.path )
//			{
//				System.out.print( " x : " + point2.x + " " );
//				System.out.println( " y : " + point2.y );
//				
//			}
			if( this.computePath( point.x, point.y, destX, destY, path ) )
				return true;
			
			else
				path.remove( path.size() - 1 );
			
//			for( final Point point3 : this.path )
//			{
//				System.out.print( " x : " + point3.x + " " );
//				System.out.println( " y : " + point3.y );
//				
//			}			
		}
		
		return false;
	}
}
