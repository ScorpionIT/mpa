package mpa.gui.menuMap;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputNamePanel extends JPanel
{
	// private MainMenuGamePanel mainMenuGamePanel;
	private JTextField textField;

	// Button button;

	public InputNamePanel()
	{
		// this.mainMenuGamePanel = mainMenuGamePanel;
		this.setLayout(null);

		FocusListener highlighter = new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e)
			{
				e.getComponent().setBackground(new Color(255, 255, 153));
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				e.getComponent().setBackground(Color.WHITE);
			}
		};
		textField = new JTextField();
		textField.addFocusListener(highlighter);

		this.add(textField);
		// button = new Button("Set Name");
		// button.addMouseListener(new MouseAdapter()
		// {
		// @Override
		// public void mouseReleased(MouseEvent e)
		// {
		// InputNamePanel.this.mainMenuGamePanel.setPlayerName(InputNamePanel.this.textField.getText());
		// }
		// });

		// button.setBackground(new Color(224, 224, 224));
		// this.add(button);

		this.setVisible(true);
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		textField.setBounds(30, 0, this.getWidth() - 80, 30);
		// button.setBounds(this.getWidth() - 190, 30, 139, 30);
	}

	public String getPlayerName()
	{
		return textField.getText();
	}
}
