package mpa.gui.menuMap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mpa.core.ai.DifficultyLevel;
import mpa.core.util.GameProperties;

public class DifficultyPanel extends JPanel
{

	private MenuSinglePlayerPanel menuSinglePlayerPanel;

	private List<AbstractButton> buttonList = new ArrayList<AbstractButton>();
	private ButtonGroup group = new ButtonGroup();
	private Image backgroundImage;
	private ActionListener actionListenerRadioButton;

	private String textImagePath = GameProperties.getInstance().getPath("TextImagePath");

	public DifficultyPanel(MenuSinglePlayerPanel menuSinglePlayerPanel)
	{
		this.menuSinglePlayerPanel = menuSinglePlayerPanel;

		initRadioButtonGroup();
		this.setLayout(null);
		this.setOpaque(false);

		actionListenerRadioButton = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

				String difficultyLevelSelected = ((JRadioButton) e.getSource()).getLabel();
				List<DifficultyLevel> somethingList = Arrays.asList(DifficultyLevel.values());

				for (DifficultyLevel difficultyLevel : somethingList)
				{
					if (difficultyLevel.name().equals(difficultyLevelSelected))
						DifficultyPanel.this.menuSinglePlayerPanel.setDifficultyLevel(difficultyLevel);
				}

			}

		};
		try
		{
			backgroundImage = ImageIO.read(new File("Assets/BackgroundImages/difficultyPanel.png"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		this.setVisible(true);

	}

	private void initRadioButtonGroup()
	{

		List<DifficultyLevel> somethingList = Arrays.asList(DifficultyLevel.values());

		for (DifficultyLevel difficultyLevel : somethingList)
		{
			JRadioButton jRadioButton = new JRadioButton(difficultyLevel.name());
			buttonList.add(jRadioButton);
			group.add(jRadioButton);
		}

	}

	private void addComponents()
	{

		int yComponent = this.getHeight() * 15 / 100;
		int xComponent = this.getWidth() * 15 / 100;

		int increment = (this.getHeight() - this.getHeight() * 30 / 100) / (buttonList.size() + 1);
		Image imageChooseDifficultyLevel = loadTextImage("chooseDifficultyLevel.png", 100);

		JLabel label = new JLabel(new ImageIcon(imageChooseDifficultyLevel));
		// label.setBounds(xComponent, yComponent, this.getWidth() - xComponent, increment);
		label.setBounds(xComponent, yComponent - this.getHeight() * 5 / 100, imageChooseDifficultyLevel.getWidth(this),
				imageChooseDifficultyLevel.getHeight(this));

		this.add(label);
		yComponent += imageChooseDifficultyLevel.getHeight(this) - imageChooseDifficultyLevel.getHeight(this) * 30 / 100;

		for (AbstractButton button : buttonList)
		{

			button.addActionListener(actionListenerRadioButton);
			button.setOpaque(false);
			button.setBounds(xComponent, yComponent, this.getWidth() - xComponent, increment);
			this.add(button);
			yComponent += increment;

		}
	}

	private Image loadTextImage(String fileName, int percentuage)
	{
		Image imageText = null;
		try
		{
			imageText = ImageIO.read(new File(textImagePath + "/" + fileName));
			imageText = imageText.getScaledInstance(imageText.getWidth(this) * percentuage / 100, imageText.getHeight(this) * percentuage / 100,
					Image.SCALE_FAST);
		} catch (IOException e)
		{
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
		return imageText;
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		addComponents();

	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);

	}
}
