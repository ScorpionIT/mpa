package mpa.gui.mapEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SubmitButtonEditorPanel extends JPanel
{

	private MainMapEditorPanel mainMapEditorPanel;
	private JButton submitButton;

	public SubmitButtonEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;
		this.setLayout(null);
		submitButton = new JButton("Submit");

		submitButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				SubmitButtonEditorPanel.this.mainMapEditorPanel.convertMapToXml();
			}

		});

		this.add(submitButton);
		this.setOpaque(false);
		this.setVisible(true);

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);

		submitButton.setBounds(this.getWidth() / 4, 0, this.getWidth() / 2, this.getHeight() / 2);
	}
}
