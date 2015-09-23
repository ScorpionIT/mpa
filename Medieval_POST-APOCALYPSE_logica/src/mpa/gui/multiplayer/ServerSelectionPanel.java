package mpa.gui.multiplayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.core.logic.MapInfo;
import mpa.core.util.GameProperties;
import mpa.core.util.MapFromXMLCreator;
import mpa.gui.multiplayer.client.BroadcastConnectionListener;
import mpa.gui.multiplayer.client.MultiplayerMenuPanel;

import org.jdom2.JDOMException;

public class ServerSelectionPanel extends JPanel {
	private static final long serialVersionUID = -8958460289373316021L;
	private Image backgroundImage;
	private int numberOfServers = 10;
	private String[] servers = new String[numberOfServers];
	private List<JLabel> serverList = new ArrayList<>();
	private JPanel selectionPanel = new JPanel();
	private JLabel selectedServer;
	private int labelHeight;
	private int numberOfServersPresent = 0;

	private JFrame mainFrame;
	private Image panelBackgroudImage;
	private int xPanel;
	private int yPanel;
	private int widthPanel;
	private int heightPanel;
	private JPanel mainMenuPanel;
	private JLabel backButton;
	private JLabel enterButton;
	private Thread thread;
	private boolean threadUpdateAlive =true;
	private BroadcastConnectionListener broadcastConnectionListener;

	public ServerSelectionPanel(final JFrame mainFrame, JPanel mainMenuPanel) {
		this.mainFrame = mainFrame;
		this.mainMenuPanel = mainMenuPanel;
		setLayout(null);
		setBounds(mainFrame.getX(), mainFrame.getY(), mainFrame.getWidth(),
				mainFrame.getHeight());

		xPanel = this.getWidth() * 20 / 100;
		yPanel = this.getHeight() * 20 / 100;
		widthPanel = this.getWidth() * 60 / 100;
		heightPanel = this.getHeight() * 60 / 100;

		addSelectionPanel();

		broadcastConnectionListener = new BroadcastConnectionListener(this);
		broadcastConnectionListener.start();
		thread = new Thread() {
		

			@Override
			public void run() {
				while (threadUpdateAlive ) {
					
					ServerSelectionPanel.this.updateUI();
					try {
						sleep(100000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		thread.start();

		try {
			backgroundImage = ImageIO.read(new File(GameProperties
					.getInstance().getPath("BackgroundImagesPath")
					+ "/background1.jpg"));
			panelBackgroudImage = ImageIO.read(new File(GameProperties
					.getInstance().getPath("BackgroundImagesPath")
					+ "/backgroundHeadquarterPanel.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		addBackButton();
		addEnterButton();

		setVisible(true);

	}


	protected void addBackButton() {
		Image imageBack = null;
		try {
			imageBack = ImageIO.read(new File(GameProperties.getInstance()
					.getPath("textImagePath") + "/back.png"));
			backButton = new JLabel(new ImageIcon(imageBack));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		backButton.setBounds(this.getWidth() * 10 / 100,
				this.getHeight() * 85 / 100, imageBack.getWidth(this),
				imageBack.getHeight(this));
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				mainFrame.setContentPane(mainMenuPanel);
				mainFrame.getContentPane().setVisible(true);
				mainFrame.setVisible(true);
				mainMenuPanel.repaint();
				mainFrame.setVisible(true);
				broadcastConnectionListener.stopListener();
				threadUpdateAlive= false;

			}
		});
		this.add(backButton);

	}

	protected void addEnterButton() {
		Image imageBack = null;
		try {
			imageBack = ImageIO.read(new File(GameProperties.getInstance()
					.getPath("textImagePath") + "/enter.png"));
			enterButton = new JLabel(new ImageIcon(imageBack));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		enterButton.setBounds(
				this.getWidth() * 90 / 100 - imageBack.getWidth(this),
				this.getHeight() * 85 / 100, imageBack.getWidth(this),
				imageBack.getHeight(this));
		enterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (selectedServer == null)
					return;

				broadcastConnectionListener.stopListener();
				threadUpdateAlive= false;
				String[] addressAndPort = selectedServer.getText().split(":");
				getMap(addressAndPort);
			}
		});
		this.add(enterButton);

	}

	private void addSelectionPanel() {
		selectionPanel.setBounds(xPanel + xPanel * 70 / 100, yPanel + yPanel
				* 40 / 100, widthPanel * 50 / 100, heightPanel * 70 / 100);
		selectionPanel.setOpaque(false);
		selectionPanel.setVisible(true);
		selectionPanel.setLayout(null);
		selectionPanel.setBorder(BorderFactory
				.createLineBorder(Color.black, 10));

		labelHeight = selectionPanel.getHeight() / (numberOfServers + 1);
		int yLabel = labelHeight / 2;

		for (int i = 0; i < numberOfServers; i++) {
			JLabel server = new JLabel("", JLabel.CENTER);
			server.setForeground(Color.WHITE);
			server.setFont(new Font("URW Chancery L", Font.BOLD, 30));
			server.setBounds(10, yLabel, selectionPanel.getWidth() - 20,
					labelHeight);

			server.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					List<JLabel> keys = ServerSelectionPanel.this.serverList;
					for (JLabel l : keys) {
						if (((JLabel) e.getSource()).equals(l)) {
						if (l.getText().equals(""))
							return;
							if (selectedServer != null)
								selectedServer.setBorder(null);
							selectedServer = ((JLabel) e.getSource());
							selectedServer.setBorder(BorderFactory
									.createLineBorder(Color.RED, 1));
							break;
						}

					}
				}

			});
			serverList.add(server);

			yLabel += labelHeight;
			selectionPanel.add(server);
		}
		this.add(selectionPanel);

	}

	public void getMap(String[] addressAndPort) {
		if (addressAndPort.length != 2)
			return;

		try {
			Socket socket;
			socket = new Socket(addressAndPort[0],
					Integer.parseInt(addressAndPort[1]));
			DataOutputStream outToServer = new DataOutputStream(
					socket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			outToServer.writeBytes("Enter" + '\n');
			String reply = inFromServer.readLine();

			
			if (reply.equals("OK")) {
				outToServer.writeBytes("GetMap" + '\n');
				reply = inFromServer.readLine();
				System.out.println(reply);

				Writer writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(GameProperties
								.getInstance().getPath(
										"MultiplayerMapPath")+"/map.xml"), "utf-8"));
				
				System.out.println(writer);

				while (!reply.equals("END")) {
					if (!reply.equals("BEGIN")) {
						writer.write(reply + '\n');
					}
					reply = inFromServer.readLine();
					System.out.println(reply);
				}

				writer.close();
				MapFromXMLCreator creator = new MapFromXMLCreator();
				MapInfo mapInfo = creator.createMapInfo(GameProperties
						.getInstance().getPath(
								"MultiplayerMapPath") + "/map.xml");
				
				
				MultiplayerMenuPanel multiplayerMenuPanel = new MultiplayerMenuPanel(getX(),
						getY(), getWidth(), getHeight(), socket,
						mapInfo, mainMenuPanel);
				
				
				mainFrame.setContentPane(multiplayerMenuPanel);
				 mainFrame.getContentPane().setVisible(true);
				 mainFrame.setVisible(true);
				 multiplayerMenuPanel.repaint();
				 mainFrame.setVisible(true);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addServer(String newServer) {
		for (JLabel server : serverList){
			if (server.getText().equals(newServer))
				return;
		}

		servers[servers.length - 1] = newServer;

//		int index = 0;
		for (JLabel server : serverList) {
			if (server.getText().equals(""))
			{
				server.setText(newServer);
				server.updateUI();
				System.out.println("ho settato "+server.getText());
				numberOfServersPresent++;
				return;
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		checkConnections();
		g.drawImage(backgroundImage, getX(), getY(), getWidth(), getHeight(),
				this);
		g.drawImage(panelBackgroudImage, xPanel, yPanel, widthPanel,
				heightPanel, this);
	}

	private void checkConnections() {

		if (numberOfServersPresent<3)
		{
			return;
		}
		for (int i = 1; i < serverList.size(); i++) {

//			if (serverList.get(i).getText().equals("")) {
//				break;
//			} else {

				String text = serverList.get(i).getText();
				serverList.get(i - 1).setText(text);
				serverList.get(i - 1).updateUI();
				serverList.get(i).setText("");
				serverList.get(i).updateUI();
				
//			}
		}
		numberOfServersPresent--;
	}

}
