package mpa.gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel
{
	private int lineGap;
	private final int lines = 10;
	private int playerX;
	private int playerY;
	private ArrayList<Point> path;
	
	public GamePanel()
	{
		this.playerX = 0;
		this.playerY = 0;
		this.path = new ArrayList<Point>();
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

        g.drawRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);
        g.setColor( Color.cyan );
        g.fillRect(playerX * this.lineGap + 1, playerY * this.lineGap + 1, lineGap - 2, lineGap - 2);

        for( final Point point : this.path )
        {
        	 g.drawRect( point.x * this.lineGap +1, point.y * this.lineGap +1, lineGap -2, lineGap -2 );
        	 g.setColor( Color.red );
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
		ArrayList<Point> borders = new ArrayList<Point>();
		
		
		if( currentX < destX )
			borders.add( new Point( currentX + 1, currentY ) );
		else if ( currentX > destX )
			borders.add( new Point( currentX - 1, currentY  ) );
		
		else if( currentY < destY )
			borders.add( new Point( currentX , currentY + 1 ) );
		else
			borders.add( new Point( currentX , currentY - 1 ) );
		
		//borders.add( new Point( currentX + 1, currentY ) );
		//borders.add( new Point( currentX + 1, currentY + 1 ) );
		//borders.add( new Point( currentX , currentY + 1 ) );
		//borders.add( new Point( currentX - 1, currentY + 1 ) );
		//borders.add( new Point( currentX - 1, currentY ) );
		//borders.add( new Point( currentX - 1, currentY - 1 ) );
		//borders.add( new Point( currentX, currentY -1 ) );
		//borders.add( new Point( currentX + 1, currentY - 1 ) );
				
		
		for( final Point point : borders )
		{
			if( path.size() <= 1 || !( point.x == path.get( path.size() - 2).x && point.y == path.get( path.size() - 2).y ) )
			{
				path.add( point );
				for( final Point point2 : this.path )
				{
					System.out.print( " x : " + point2.x + " " );
					System.out.println( " y : " + point2.y );
					
				}
				if( this.computePath( point.x, point.y, destX, destY, path ) )
					return true;
				
				else
					path.remove( path.size() - 1 );
				
				for( final Point point3 : this.path )
				{
					System.out.print( " x : " + point3.x + " " );
					System.out.println( " y : " + point3.y );
					
				}
			}
			
		}
		
		return false;
	}

}
