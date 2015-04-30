package mpa.gui.mapEditor;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mpa.core.util.GameProperties;

public class SettingsMapEditorPanel extends JPanel
{
	private ArrayList<JLabel> jLabels = new ArrayList<>();

	private String[] textJLabels = { "Map Name", "Map Width", "Map Height" };

	private JLabel widthLabel;
	private JLabel heightLabel;

	private JTextField mapNameField = new JTextField();
	private MainMapEditorPanel mainMapEditorPanel;

	private final int increment = 50;
	private final int minDimension = 500;
	private boolean setBounds = false;
	private HashMap<String, Image> images = new HashMap<String, Image>();

	public SettingsMapEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{

		this.mainMapEditorPanel = mainMapEditorPanel;
		this.setLayout(null);
		addWidthLabel();
		addMapNameField();
		addHeightLabel();

		this.mainMapEditorPanel.setMapDimension(minDimension, minDimension);
		try
		{
			images.put("Plus", ImageIO.read(new File(GameProperties.getInstance().getPath("PannelIconPath") + "/plusIcon.png")));
			images.put("Minus", ImageIO.read(new File(GameProperties.getInstance().getPath("PannelIconPath") + "/minusIcon.png")));

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		for (String field : textJLabels)
		{

			JLabel jLabel = new JLabel(field);
			jLabel.setOpaque(false);

			jLabels.add(jLabel);
			this.add(jLabel);

		}
		this.setVisible(true);
	}

	public String getMapName()
	{
		return mapNameField.getText();
	}

	private void addWidthLabel()
	{
		widthLabel = new JLabel(Integer.toString(minDimension));
		widthLabel.setOpaque(false);
		this.add(widthLabel);

	}

	private void addHeightLabel()
	{
		heightLabel = new JLabel(Integer.toString(minDimension));
		heightLabel.setOpaque(false);
		this.add(heightLabel);

	}

	private void addMapNameField()
	{
		mapNameField = new JTextField();
		mapNameField.setOpaque(false);
		this.add(mapNameField);

	}

	private void setComponentsBounds()
	{

		setBounds = true;
		int numberOfComponents = jLabels.size() * 2;

		int increment = (this.getHeight() - (this.getHeight() * 20 / 100)) / numberOfComponents;

		int yComponent = this.getHeight() * 10 / 100;
		int xComponent = this.getWidth() * 10 / 100;
		int width = this.getWidth() * 80 / 100;

		images.put("Plus", images.get("Plus").getScaledInstance(increment / 2, increment / 2, 0));
		images.put("Minus", images.get("Minus").getScaledInstance(increment / 2, increment, 0));
		for (int i = 0; i < jLabels.size(); i++)
		{

			jLabels.get(i).setBounds(xComponent, yComponent, width, increment);
			yComponent += increment;
			if (i == 0)
			{
				mapNameField.setBounds(xComponent, yComponent, width, increment);
				yComponent += increment;
			}
			if (i == 1)
			{
				addButtonListener(widthLabel, yComponent, xComponent, increment);
				yComponent += increment;
			}
			if (i == 2)
			{
				addButtonListener(heightLabel, yComponent, xComponent, increment);
				yComponent += increment;
			}
		}

		// TODO mettere i bottoni + e - alla larghezza e alla lunghezza e mettere i listener per
		// mario

	}

	private void addButtonListener(final JLabel label, int yComponent, int xComponent, int height)
	{

		JButton plusButton = new JButton(new ImageIcon(images.get("Plus")));
		JButton minusButton = new JButton(new ImageIcon(images.get("Minus")));
		plusButton.setOpaque(false);
		plusButton.setBorderPainted(false);
		plusButton.setContentAreaFilled(false);
		minusButton.setOpaque(false);
		minusButton.setBorderPainted(false);
		minusButton.setContentAreaFilled(false);

		plusButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				int mapDimension = Integer.parseInt(label.getText());
				mapDimension += SettingsMapEditorPanel.this.increment;
				label.setText(Integer.toString(mapDimension));
				mainMapEditorPanel.setMapDimension(Integer.parseInt(widthLabel.getText()), Integer.parseInt(heightLabel.getText()));
				SettingsMapEditorPanel.this.repaint();

			}
		});
		minusButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				int mapDimension = Integer.parseInt(label.getText());
				mapDimension -= SettingsMapEditorPanel.this.increment;
				if (mapDimension >= minDimension)
				{
					label.setText(Integer.toString(mapDimension));
					mainMapEditorPanel.setMapDimension(Integer.parseInt(widthLabel.getText()), Integer.parseInt(heightLabel.getText()));
					SettingsMapEditorPanel.this.repaint();
				}

			}
		});

		label.setBounds(xComponent, yComponent, this.getWidth() * 65 / 100, height);
		plusButton.setBounds(label.getWidth() + xComponent, yComponent, height / 2, height / 2);
		minusButton.setBounds(plusButton.getX() + plusButton.getWidth() + plusButton.getWidth() / 2, yComponent, height / 2, height / 2);
		SettingsMapEditorPanel.this.add(plusButton);
		SettingsMapEditorPanel.this.add(minusButton);

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		if (!setBounds)
			setComponentsBounds();

		repaint();

	}

}
