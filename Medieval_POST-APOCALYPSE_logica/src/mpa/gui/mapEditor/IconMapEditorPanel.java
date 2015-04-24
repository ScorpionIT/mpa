package mpa.gui.mapEditor;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mpa.core.logic.Pair;

public class IconMapEditorPanel extends JScrollPane
{

	private JPanel iconPanel = new JPanel();
	private MainMapEditorPanel mainMapEditorPanel;
	private HashMap<String, Image> images;
	private ArrayList<Pair<String, Rectangle>> imageLabelsPosition = new ArrayList<>();

	public IconMapEditorPanel(MainMapEditorPanel mainMapEditorPanel, HashMap<String, Image> imageLabels)
	{

		this.mainMapEditorPanel = mainMapEditorPanel;
		this.images = imageLabels;

		iconPanel.setLayout(null);
		this.setLayout(null);

		this.setOpaque(false);

		this.add(iconPanel);
		iconPanel.setVisible(true);
		this.setVisible(true);

	}

	public JPanel getIconPanel()
	{
		return iconPanel;
	}

	public ArrayList<Pair<String, Rectangle>> getImageLabelsPosition()
	{
		return imageLabelsPosition;
	}

	private void addImageIcon()
	{

		int yComponent = 40;
		Set<String> keySet = images.keySet();
		for (String key : keySet)
		{
			ImageIcon imageIcon = new ImageIcon(images.get(key));
			JLabel jLabel = new JLabel(imageIcon);
			Rectangle rect = new Rectangle(iconPanel.getWidth() * 50 / 100 - (imageIcon.getIconWidth() / 2), yComponent, imageIcon.getIconWidth(),
					imageIcon.getIconHeight());
			// jLabel.addMouseListener(listener);
			// jLabel.setTransferHandler(new TransferHandler("icon"));
			imageLabelsPosition.add(new Pair<String, Rectangle>(key, rect));
			jLabel.setBounds(rect);
			yComponent += imageIcon.getIconHeight() + 40;

			iconPanel.add(jLabel);
		}

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		// TODO Stub di metodo generato automaticamente
		super.setBounds(x, y, width, height);
		this.iconPanel.setBounds(0, 0, width, height);
		addImageIcon();
	}

}
