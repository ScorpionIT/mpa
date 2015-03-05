package mpa.gui.menuMap;

import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputNamePanel extends JPanel
{
	private MainMenuPanel mainMenuPanel;
	private JTextField textField;
	Button button;

	public InputNamePanel(MainMenuPanel mainMenuPanel)
	{
		this.mainMenuPanel = mainMenuPanel;
		this.setLayout(null);

		textField = new JTextField();
		this.add(textField);
		button = new Button("Set Name");
		button.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				InputNamePanel.this.mainMenuPanel.setPlayerName(InputNamePanel.this.textField.getText());
			}
		});
		this.add(button);

		this.setVisible(true);
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		textField.setBounds(30, 0, this.getWidth() - 80, 30);
		button.setBounds(this.getWidth() - 190, 30, 139, 30);
	}

}
