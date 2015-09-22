package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class SubmitButtonEditorPanel extends JPanel
{

	private MainMapEditorPanel mainMapEditorPanel;
	private JButton submitButton;

	public SubmitButtonEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;
		this.setLayout(null);

		Color button = new Color(1f, 1f, 0f, 0.5f);
		UIManager.put("Button.font", new Font("Comic Sans MS", Font.BOLD, 15));
		UIManager.put("Button.background", button);

		submitButton = new JButton("Submit");

		submitButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				SubmitButtonEditorPanel.this.mainMapEditorPanel.convertMapToXml();
				SubmitButtonEditorPanel.this.mainMapEditorPanel.backButton();
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
