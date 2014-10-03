package mpa.gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class GamePanel extends JPanel
{
	private int lineGap;
	private final int lines = 10;
	private int playerX;
	private int playerY;
	
	public GamePanel()
	{
		this.playerX = 0;
		this.playerY = 0;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int width = getWidth();
        int height = getHeight();
        System.out.println(width);
        System.out.println(height);
		lineGap = getWidth()/this.lines;
		System.out.println(lineGap);

        
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

        g.drawRect(playerX + 1, playerY + 1, lineGap - 2, lineGap - 2);
        g.setColor( Color.cyan );
        g.fillRect(playerX + 1, playerY + 1, lineGap - 2, lineGap - 2);

	}

}
