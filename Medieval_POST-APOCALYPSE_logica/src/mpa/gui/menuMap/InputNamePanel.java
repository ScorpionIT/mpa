package mpa.gui.menuMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputNamePanel extends JPanel
{
	// private MainMenuGamePanel mainMenuGamePanel;
	private JTextField textField;
	private JLabel insertYourNameLabel;

	// Button button;

	public InputNamePanel()
	{
		// this.mainMenuGamePanel = mainMenuGamePanel;
		this.setLayout(null);

		insertYourNameLabel = new JLabel("Insert your name");
		insertYourNameLabel.setForeground(Color.WHITE);
		insertYourNameLabel.setFont(new Font("URW Chancery L", Font.BOLD, 20));

		this.add(insertYourNameLabel);

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
		insertYourNameLabel.setBounds(width * 2 / 100, 0, width * 80 / 100, height * 10 / 100);
		textField.setBounds(width * 2 / 100, insertYourNameLabel.getY() + insertYourNameLabel.getHeight(), this.getWidth() - this.getWidth() * 10
				/ 100, height * 15 / 100);
	}

	public String getPlayerName()
	{
		return textField.getText();
	}
}
