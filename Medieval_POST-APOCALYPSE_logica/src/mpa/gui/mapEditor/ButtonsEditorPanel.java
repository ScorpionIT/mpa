package mpa.gui.mapEditor;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsEditorPanel extends JPanel
{
	private JButton undo;
	private JButton redo;
	private JButton back;
	private MainMapEditorPanel mainMapEditorPanel;

	public ButtonsEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;

		this.setLayout(null);
		undo = new JButton("Undo");
		undo.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				ButtonsEditorPanel.this.mainMapEditorPanel.undo();
			}
		});
		redo = new JButton("Redo");
		redo.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				ButtonsEditorPanel.this.mainMapEditorPanel.redo();
			}
		});
		back = new JButton("Back");

		this.add(undo);
		this.add(redo);
		this.add(back);
		this.setVisible(true);

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		// TODO Stub di metodo generato automaticamente
		super.setBounds(x, y, width, height);
		int xIncrement = this.getWidth() / 4;
		int xComponent = xIncrement / 2;
		back.setBounds(xComponent, this.getHeight() * 20 / 100, xIncrement, this.getHeight() - this.getHeight() * 40 / 100);
		xComponent += xIncrement;
		undo.setBounds(xComponent, this.getHeight() * 20 / 100, xIncrement, this.getHeight() - this.getHeight() * 40 / 100);
		xComponent += xIncrement;
		redo.setBounds(xComponent, this.getHeight() * 20 / 100, xIncrement, this.getHeight() - this.getHeight() * 40 / 100);

	}
}
