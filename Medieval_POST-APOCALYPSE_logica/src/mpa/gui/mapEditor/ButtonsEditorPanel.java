package mpa.gui.mapEditor;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import mpa.core.util.GameProperties;

public class ButtonsEditorPanel extends JPanel
{
	private JButton undo;
	private JButton redo;
	private JButton back;
	private MainMapEditorPanel mainMapEditorPanel;
	private HashMap<String, Image> images = new HashMap<String, Image>();
	private boolean setBounds = false;

	public ButtonsEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;

		this.setLayout(null);

		try
		{
			images.put("Undo", ImageIO.read(new File(GameProperties.getInstance().getPath("PannelIconPath") + "/undoIcon1.png")));
			images.put("Redo", ImageIO.read(new File(GameProperties.getInstance().getPath("PannelIconPath") + "/redoIcon.png")));
			images.put("Back", ImageIO.read(new File(GameProperties.getInstance().getPath("PannelIconPath") + "/backIcon.png")));

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		this.setVisible(true);

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		if (!setBounds)
			setComponentsBounds();

	}

	private void setComponentsBounds()
	{
		setBounds = true;
		int xIncrement = this.getWidth() / 4;
		int xComponent = xIncrement / 2;
		int buttonDimension = this.getHeight() - this.getHeight() * 10 / 100;
		images.put("Undo", images.get("Undo").getScaledInstance(buttonDimension, buttonDimension, 0));
		// images.put("Undo", images.get("Undo").getScaledInstance(xIncrement, this.getHeight() -
		// this.getHeight() * 40 / 100, 0));
		images.put("Redo", images.get("Redo").getScaledInstance(buttonDimension, buttonDimension, 0));
		images.put("Back", images.get("Back").getScaledInstance(buttonDimension, buttonDimension, 0));

		undo = new JButton(new ImageIcon(images.get("Undo")));
		undo.setOpaque(false);
		undo.setContentAreaFilled(false);
		undo.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				ButtonsEditorPanel.this.mainMapEditorPanel.undo();
			}
		});
		redo = new JButton(new ImageIcon(images.get("Redo")));
		redo.setOpaque(false);
		redo.setContentAreaFilled(false);
		redo.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				ButtonsEditorPanel.this.mainMapEditorPanel.redo();
			}
		});
		back = new JButton(new ImageIcon(images.get("Back")));
		back.setOpaque(false);
		back.setContentAreaFilled(false);

		this.add(undo);
		this.add(redo);
		this.add(back);

		back.setBounds(xComponent, this.getHeight() * 5 / 100, buttonDimension, buttonDimension);
		xComponent += xIncrement * 2;
		undo.setBounds(xComponent, this.getHeight() * 5 / 100, buttonDimension, buttonDimension);
		// undo.setBounds(xComponent, this.getHeight() * 20 / 100, xIncrement, this.getHeight() -
		// this.getHeight() * 40 / 100);
		// xComponent += xIncrement;
		redo.setBounds(undo.getX() + undo.getWidth(), this.getHeight() * 5 / 100, buttonDimension, buttonDimension);

	}
}
