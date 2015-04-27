package mpa.gui.mapEditor;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mpa.core.logic.Pair;

public class SettingsMapEditorPanel extends JPanel
{
	private LinkedHashMap<String, Pair<JTextField, JLabel>> fields = new LinkedHashMap();

	private String[] textFields = { "Map Name", "Map Width", "Map Height" };

	private MainMapEditorPanel mainMapEditorPanel;
	private JButton submit;

	public SettingsMapEditorPanel(MainMapEditorPanel mainMapEditorPanel)
	{

		this.mainMapEditorPanel = mainMapEditorPanel;
		this.setLayout(null);
		submit = new JButton("Set Properties");
		submit.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				String width = fields.get("Map Width").getFirst().getText();
				String height = fields.get("Map Height").getFirst().getText();
				if (checkInputValue(width, height))

				{

					SettingsMapEditorPanel.this.mainMapEditorPanel.setMapDimension(Integer.parseInt(width), Integer.parseInt(height));
				}

			}
		});
		this.add(submit);
		for (String field : textFields)
		{

			JTextField textField = new JTextField();
			textField.setOpaque(false);
			JLabel jLabel = new JLabel(field);
			jLabel.setOpaque(false);
			this.add(textField);
			this.add(jLabel);
			fields.put(field, new Pair<JTextField, JLabel>(textField, jLabel));
		}
		this.setVisible(true);
	}

	public String getMapName()
	{
		return fields.get("Map Name").getFirst().getText();
	}

	private void setComponentsBounds()
	{

		int numberOfComponents = fields.size() * 2;

		int increment = (this.getHeight() - (this.getHeight() * 20 / 100)) / numberOfComponents;

		int yComponent = this.getHeight() * 10 / 100;
		int xComponent = this.getWidth() * 10 / 100;
		int width = this.getWidth() * 80 / 100;

		Set<String> keySet = fields.keySet();

		for (String key : keySet)
		{
			Pair<JTextField, JLabel> pair = fields.get(key);
			pair.getSecond().setBounds(xComponent, yComponent, width, increment);
			yComponent += increment;
			pair.getFirst().setBounds(xComponent, yComponent, width, increment);
			yComponent += increment;

		}
		submit.setBounds(width + xComponent - submit.getWidth(), yComponent + (this.getHeight() * 2 / 100), 140, 20);
	}

	private boolean checkInputValue(String width, String height)
	{
		if (width.equals("") || Integer.parseInt(width) < 500)
		{
			JOptionPane.showMessageDialog(new Frame(), "Inserisci la larghezza", "", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		else if (height.equals("") || Integer.parseInt(height) < 500)
		{
			JOptionPane.showMessageDialog(new Frame(), "Inserisci l'altezza", "", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		setComponentsBounds();
		repaint();

	}

}
